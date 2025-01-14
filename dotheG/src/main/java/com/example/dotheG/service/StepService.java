package com.example.dotheG.service;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.step.StepRewardStateResponseDto;
import com.example.dotheG.dto.step.StepSummaryResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.Step;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.StepRepository;
import com.example.dotheG.service.step.RewardStrategy;
import com.example.dotheG.service.step.TodayStepRewardStrategy;
import com.example.dotheG.service.step.WeeklyStepRewardStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StepService {

    private final MemberService memberService;
    private final StepRepository stepRepository;
    private final MemberInfoRepository memberInfoRepository;


    @Transactional
    public void resetTodayStep() {
        List<Step> steps = stepRepository.findAll();
        for (Step step : steps) {
            step.resetTodayStep();
        }

        log.info("모든 사용자의 일일 걸음수 초기화 완료");
    }

    @Transactional
    public void resetWeeklyStep() {
        List<Step> steps = stepRepository.findAll();
        for (Step step : steps) {
            step.resetWeeklyStep();
        }

        log.info("모든 사용자의 주간 걸음수 초기화 완료");
    }

    // 걸음수 업데이트 (누적 걸음수 받으면 누적-기존으로 추가합산)
    @Transactional
    public int updateStep(int walkingCount) {
        // 로그인 유저 정보 찾기
        Member member = memberService.getCurrentMember();

        // step 객체 찾기
        Step step = loadStep(member);

        // 걸음수 업데이트
        step.updateStep(walkingCount);

        // 저장
        stepRepository.save(step);

        return walkingCount;
    }

    // 만보기 요약 보고서 반환
    @Transactional
    public StepSummaryResponseDto getStepSummary() {
        // 로그인 유저 정보 불러오기
        Member member = memberService.getCurrentMember();

        // step 객체 불러오기
        Step step = loadStep(member);

        // 일일, 주간, 누적, 탄소절감량 반환
        StepSummaryResponseDto responseDto = new StepSummaryResponseDto(
                step.getTodayStep(),
                step.getWeeklyStep(),
                step.getTotalStep(),
                getCarbonReduction(step.getWeeklyStep())
        );

        return responseDto;
    }

    // 만보기 목표달성시 리워드 지급(일일, 주간)
    @Transactional
    public Response<Object> getReward(String period) {

        // 로그인 유저 불러오기
        Member member = memberService.getCurrentMember();

        // step 객체 불러오기
        Step step = loadStep(member);

        // 전략 패턴으로 today 와 weekly중 상황에 맞는 보상지급방식 동적할당
        RewardStrategy rewardStrategy = getRewardStrategy(period);
        if (rewardStrategy == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        return handleReward(member,step, rewardStrategy);
    }

    private RewardStrategy getRewardStrategy(String period) {
        switch (period) {
            case "weekly":
                return new WeeklyStepRewardStrategy();
            case "today":
                return new TodayStepRewardStrategy();
            default:
                return null;
        }
    }

    private Response<Object> handleReward(Member member, Step step, RewardStrategy rewardStrategy) {
        if (rewardStrategy.getCurrentSteps(step) < rewardStrategy.getRequiredSteps()) {
            // 걸음수 부족 에러
            throw new CustomException(ErrorCode.INSUFFICIENT_STEP_COUNT);
        }

        if (rewardStrategy.isRewardGiven(step)) {
            // 이미 리워드 지급
            throw new CustomException(ErrorCode.REWARD_ALREADY_GRANTED);
        }

        // 리워드 지급
        Optional<MemberInfo> memberInfoOptional = memberInfoRepository.findByUserId(member);
        MemberInfo memberInfo = memberInfoOptional.orElseThrow();
        memberInfo.addReward(rewardStrategy.getRewardPoints());
        //memberInfoRepository.save(memberInfo);

        rewardStrategy.setRewardGiven(step);
        //stepRepository.saveAndFlush(step);
        return Response.success("리워드 지급 완료", rewardStrategy.getRewardPoints());
    }

    // 탄소배출량 계산
    private double getCarbonReduction(int weeklyStepCount) {
        double temp = (weeklyStepCount / 1000.0) * 200;  // 1000보당 200g 절약
        double carbonReduction = temp / 1000.0;          // kg로 환산
        return carbonReduction;
    }

    // 리워드 지급 상태 반환
    public StepRewardStateResponseDto getStepRewardState() {
        // 로그인 유저 정보 불러오기
        Member member = memberService.getCurrentMember();

        // step 객체 불러오기
        Step step = loadStep(member);

        // today, weekly 보상지급 상태 반환
        StepRewardStateResponseDto responseDto = new StepRewardStateResponseDto(
                step.isTodayMissionComplete(),
                step.isWeeklyMissionComplete()
        );

        return responseDto;
    }

    // step 객체 호출 함수
    private Step loadStep(Member member) {
        Optional<Step> stepOptional = stepRepository.findByUserId(member);
        Step step = stepOptional.orElseThrow(() -> new CustomException(ErrorCode.STEP_NOT_FOUND));
        return step;
    }
}
