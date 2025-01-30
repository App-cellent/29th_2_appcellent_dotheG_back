package com.example.dotheG.service;

import com.example.dotheG.dto.activity.ActivityListResponse;
import com.example.dotheG.dto.activity.ActivityResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Activity;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberActivity;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.repository.ActivityRepository;
import com.example.dotheG.repository.MemberActivityRepository;
import com.example.dotheG.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final MemberActivityRepository memberActivityRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberService memberService;

    // OpenCV 사용 (사진 분석)
    public Long analyzePhoto(MultipartFile activityImage){
        return 1L;
    }

    // 퀘스트 등록하기 (사진 결과 저장)
    // [이미지, 활동 명, 날짜] 저장되어야 함 + 리워드 지급 받아야 함
    @Transactional
    public void requestImage(MultipartFile activityImage) {
        // 사용자 정보 조회
        MemberInfo memberInfo = memberService.getCurrentMemberInfo();

        // 사진 분석하여 활동 명 결정
        Long analyzedActivityId = analyzePhoto(activityImage);
        // Fixme 이거 뭔가 두 메소드를 연결하는 사이에 문제가 있을 것 같아

        // 활동 조회
        Activity activity = activityRepository.findById(analyzedActivityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        byte[] imageBytes;
        try {
            // MultipartFile을 byte[]로 변환
            imageBytes = activityImage.getBytes();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PROCESS_IMAGE_FAILED);
        }

        // 새로운 MemberActivity 객체 생성
        MemberActivity memberActivity = new MemberActivity(
                null,
                memberInfo.getUserId(),
                activity,
                activity.getActivityName(), // 사진을 분석한 결과 활동명
                imageBytes,
                LocalDate.now()
        );

        // MemberActivity 저장
        memberActivityRepository.save(memberActivity);

        // 리워드 적립
        memberInfoRepository.save(new MemberInfo(
                memberInfo.getUserInfoId(),
                memberInfo.getUserId(),
                memberInfo.getUserReward() + activity.getActivityReward(),
                memberInfo.getMainChar()
        ));
    }

    public ActivityListResponse viewToday() {
        Member member = memberService.getCurrentMember();
        String userName = member.getUserName();

        // 사용자 ID와 날짜에 해당하는 member_activity를 불러오기
        List<MemberActivity> activities = memberActivityRepository.findByUserIdAndActivityDate(member, LocalDate.now());

        List<ActivityResponseDto> activityResponseDtos = activities.stream()
                .map(activity -> new ActivityResponseDto(activity.getActivityId().getActivityId(), activity.getActivityName(), activity.getActivityImage()))
                .collect(Collectors.toList());  // 활동이 있을 경우 리스트 반환

        // ActivityResponseDto 리스트로 변환
        if (activities.isEmpty()) {
            return new ActivityListResponse(userName, 0, null);
        } else{
            return new ActivityListResponse(userName, activities.size(), activityResponseDtos);
        }

    }

}
