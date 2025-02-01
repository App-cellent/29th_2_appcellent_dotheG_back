package com.example.dotheG.service;

import com.example.dotheG.dto.MainpageResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Activity;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.Step;
import com.example.dotheG.repository.ActivityRepository;
import com.example.dotheG.repository.StepRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainpageService {

    private final MemberService memberService;
    private final ActivityRepository activityRepository;
    private final StepRepository stepRepository;
    private final StepService stepService;

    public MainpageResponseDto getInfo() {
        Member member = memberService.getCurrentMember();
        MemberInfo memberInfo = memberService.getCurrentMemberInfo();

        Optional<Step> stepOptional = stepRepository.findByUserId(member);
        Step step = stepOptional.orElseThrow(() -> new CustomException(ErrorCode.STEP_NOT_FOUND));

        // 이번달/지금까지 지킨 나무 - member
        double monthSavedTree = calculateSavedTree(stepService.getCarbonReduction(step.getMonthlyStep()));
        double totalSavedTree = calculateSavedTree(stepService.getCarbonReduction(step.getTotalStep()));

        // 데일리/스페셜 퀘스트 (랜덤)
        // Fixme : 하루 마다 고정으로 해야 하나? (현재 : 로딩할 때마다 랜덤 생성)
        Activity dailyActivity = activityRepository.findRandomActivity(1, 10);
        Activity specialActivity = activityRepository.findRandomActivity(11, 20);

        return new MainpageResponseDto(
                member.getUserName(),
                memberInfo.getUserReward(),
                memberInfo.getMainChar(),
                monthSavedTree,
                totalSavedTree,
                dailyActivity.getActivityId(),
                specialActivity.getActivityId()
        );
    }

    public boolean getTutorial(){
        Member member = memberService.getCurrentMember();
        return member.isTutorial();
    }

    @Transactional
    public void changeTutorial(){
        Member member = memberService.getCurrentMember();
        member.changeTutorial();
    }

    // {나의 탄소 절감량}/22,000g  (소수 두 번째자리에서 반올림)
    private double calculateSavedTree(double savedCarbon) {
        return Math.round((savedCarbon / 22) * 100 ) / 100.0; // 소수점 두 번째 자리 반올림
    }
}
