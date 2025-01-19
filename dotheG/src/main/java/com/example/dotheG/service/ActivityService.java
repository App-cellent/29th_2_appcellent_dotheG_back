package com.example.dotheG.service;

//import org.opencv.core.*;
//import org.opencv.imgproc.Imgproc;

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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
//        try {
//            // 1. MultipartFile -> BufferedImage로 변환
//            InputStream inputStream = activityImage.getInputStream();
//            BufferedImage bufferedImage = ImageIO.read(inputStream);
//
//            // 2. BufferedImage -> Mat 변환
//            Mat image = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
//            byte[] data = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
//            image.put(0, 0, data);
//
//            // 3. 이미지를 축소하여 주요 색상 추출
//            Mat resizedImage = new Mat();
//            Size size = new Size(50, 50); // 크기를 축소하여 주요 색상 분석
//            Imgproc.resize(image, resizedImage, size);
//
//            // 4. 이미지를 HSV로 변환
//            Mat hsvImage = new Mat();
//            Imgproc.cvtColor(resizedImage, hsvImage, Imgproc.COLOR_BGR2HSV);
//
//            // 5. 색상 히스토그램 분석
//            Mat hist = new Mat();
//            Imgproc.calcHist(
//                    Arrays.asList(hsvImage),
//                    new MatOfInt(0), // Hue 채널만 분석
//                    new Mat(),
//                    hist,
//                    new MatOfInt(180), // Hue 범위
//                    new MatOfFloat(0, 180)
//            );
//
//            // 6. 히스토그램에서 가장 높은 값의 Hue 추출
//            Core.MinMaxLocResult mmr = Core.minMaxLoc(hist);
//            double dominantHue = mmr.maxLoc.y;
//
//            // 7. Hue 값에 따라 활동 매핑
//            if (dominantHue >= 0 && dominantHue <= 30) { // 빨강 계열
//                return 1L; // 활동 ID 1 (예: "환경 정화 활동")
//            } else if (dominantHue > 30 && dominantHue <= 90) { // 초록 계열
//                return 2L; // 활동 ID 2 (예: "재활용 분리 활동")
//            } else if (dominantHue > 90 && dominantHue <= 150) { // 파랑 계열
//                return 3L; // 활동 ID 3 (예: "공원 걷기 활동")
//            } else {
//                return 4L; // 기본 활동
//            }
//        } catch (IOException e) {
//            throw new CustomException(ErrorCode.PROCESS_IMAGE_FAILED);
//        }
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

    public List<ActivityResponseDto> viewToday() {
        Member member = memberService.getCurrentMember();

        // 사용자 ID와 날짜에 해당하는 member_activity를 불러오기
        List<MemberActivity> activities = memberActivityRepository.findByUserIdAndActivityDate(member, LocalDate.now());

        if (activities.isEmpty()) {
            throw new CustomException(ErrorCode.MYACTIVITY_NOT_FOUND);
            // Todo 이거 false 메세지가 아니라 true(활동 없음)로 출력할 수 있어야하나?
        }

        // ActivityResponseDto 리스트로 변환
        return activities.stream()
                .map(activity -> new ActivityResponseDto(activity.getActivityName(), activity.getActivityImage()))
                .collect(Collectors.toList());  // 활동이 있을 경우 리스트 반환
    }

}
