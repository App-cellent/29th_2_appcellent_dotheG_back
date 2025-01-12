package com.example.dotheG.service;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.step.StepSummaryResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.Step;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.MemberRepository;
import com.example.dotheG.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {

    private final MemberService memberService;
    private final StepRepository stepRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;

    // 기존유저용 (12시 지나면 step객체 생성)
    @Transactional
    public void createStepForAllUsers() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getUserName() = " + member.getUserName());
            Optional<Step> existingStep = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
            if (existingStep.isEmpty()) {
                createStep(member);
            }
        }

    }

    public void createStep(Member member) {
        Step step = new Step(member, LocalDate.now());
        stepRepository.save(step);
    }

    // 걸음수 업데이트
    @Transactional
    public int updateStep(int walkingCount) {
        // 로그인 유저 정보 찾기
        Member member = memberService.getCurrentMember();

        // step객체 업데이트
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
        Step step = stepOptional.orElseThrow(()-> new CustomException(ErrorCode.STEP_NOT_FOUND));
        step.updateStepCount(walkingCount);
        stepRepository.save(step);

        return step.getStepCount();
    }

    // 만보기 요약 보고서 반환
    public StepSummaryResponseDto getStepSummary() {
        // 로그인 유저 정보 불러오기
        Member member = memberService.getCurrentMember();

        int todaySteps = getTodayStepCount(member);
        int weeklySteps = getWeekStepCount(member);
        //int monthlySteps = getMonthStepCount(member, LocalDate.now());
        int totalSteps = getTotalStepCount(member);
        double carbonReduction = getCarbonReduction(weeklySteps);

        StepSummaryResponseDto responseDto = new StepSummaryResponseDto(
                todaySteps,
                weeklySteps,
                //monthlySteps,
                totalSteps,
                carbonReduction
        );

        return responseDto;
    }

    // 만보기 목표달성시 리워드 지급
    @Transactional
    public Response<Object> getReward() {

        // 로그인 유저 불러오기
        Member member = memberService.getCurrentMember();

        // 오늘 step 객체 조회
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
        Step step = stepOptional.orElseThrow(()-> new CustomException(ErrorCode.STEP_NOT_FOUND));

        // 걸음수, 미션완료 여부 가져오기
        int todaySteps = step.getStepCount();
        boolean isComplete = step.isComplete();

        // FIXME 주간 인증 추가 리워드 10
        // TODO 리워드 지급 함수 분리하기
        if (todaySteps >= 7000) {
            if (isComplete) {
                // 이미 리워드가 지급된 상태
                throw new CustomException(ErrorCode.REWARD_ALREADY_GRANTED);
            }
            // 리워드 지급
            Optional<MemberInfo> memberInfoOptional = memberInfoRepository.findByUserId(member);
            MemberInfo memberInfo = memberInfoOptional.orElseThrow(()->new CustomException(ErrorCode.MEMBER_INFO_NOT_FOUND));
            memberInfo.addReward(3);
            memberInfoRepository.save(memberInfo);

            // 미션완료 변경
            step.updateisComplete();

            return Response.success("리워드 지급 완료", 3);

        }

        // 걸음수 부족한상태로 리워드 요청
        throw new CustomException(ErrorCode.INSUFFICIENT_STEP_COUNT);
    }

    // 오늘 걸음수
    private int getTodayStepCount(Member member) {
        LocalDate today = LocalDate.now();
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, today);
        Step step = stepOptional.orElseThrow(()-> new CustomException(ErrorCode.STEP_NOT_FOUND));
        return step.getStepCount();
    }

    // 오늘 날짜를 가지고 이번주의 월요일 날짜 찾기
    private LocalDate getStartOfWeek(LocalDate today) {
        // 오늘이 월요일 -> 오늘 날짜 반환
        if (today.getDayOfWeek() == DayOfWeek.MONDAY) {
            return today;
        }

        // 오늘이 수요일 -> 3일 전인 월요일 날짜 반환
        return today.minusDays(today.getDayOfWeek().getValue() - 1);
    }

    // 주간 집계
    private int getWeekStepCount(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = getStartOfWeek(today); // 월요일부터
        return stepRepository.findWeeklyStepsByMember(member, startOfWeek, today); // 현재요일까지
    }

    // 월간 집계
    // TODO 제거
    private int getMonthStepCount(Member member) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1); // 이번달 1일부터
        return stepRepository.findMonthlyStepsByMember(member, startOfMonth, today); // 현재날짜까지
    }

    // 누적 집계
    private int getTotalStepCount(Member member) {
        return stepRepository.findTotalStepsByMember(member);
    }

    // 탄소배출량 계산
    private double getCarbonReduction(int weeklyStepCount) {
        double temp = (weeklyStepCount / 1000.0) * 200;  // 1000보당 200g 절약
        double carbonReduction = temp / 1000.0;          // kg로 환산

        return carbonReduction;
    }

}
