package com.example.dotheG.service;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.step.StepSummaryResponseDto;
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

    private final StepRepository stepRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    // 기존유저용 (12시 지나면 step객체 생성)
    @Transactional
    public void createStepForAllUsers() {
        // FIXME 실제유저 데이터베이스에서 호출하는것으로 변경 필요
        // FIXME 현재 null로 작성되어있는 회원전체목록 불러오기 변경 필요
        List<Member> members = null;
        for (Member member : members) {
            Optional<Step> existingStep = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
            if (existingStep.isEmpty()) {
                createStep(member);
            }
        }

    }

    // 신규유저용(회원가입시 객체생성)
    // TODO 회원가입 로직에 step객체 생성하는 로직 추가
    public void createStep(Member member) {
        Step step = new Step(member, LocalDate.now());
        stepRepository.save(step);
    }

    // TODO 기능추가구현
    // 기본걸음수 반환 필요한지?

    // 걸음수 업데이트
    @Transactional
    public int updateStep(Long userId, int walkingCount) {
        // FIXME Authentication.getUserName()으로 user정보 받아오기
        Member member = memberRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을수없습니다."));

        // step객체 업데이트
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
        Step step = stepOptional.orElseThrow(()-> new IllegalStateException("업데이트할 step객체가 없습니다."));
        step.updateStepCount(walkingCount);
        stepRepository.save(step);

        // TODO 반환을 필요?
        return step.getStepCount();
    }

    // 만보기 요약 보고서 반환
    // TODO month통계는 뺴기
    public StepSummaryResponseDto getStepSummary(Long userId) {
        // FIXME Authentication.getUserName()으로 user정보 받아오기
        Member member = memberRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을수없습니다."));

        // FIXME 매번 조회할때마다 search하는게 비효율적일거같은데, erd추가하는거 고민해보기
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
    public Response<Object> getReward(Long userId) {
        // FIXME Authentication.getUserName()으로 user정보 받아오기
        Member member = memberRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을수없습니다."));


        // 오늘 step 객체 조회
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, LocalDate.now());
        Step step = stepOptional.orElseThrow(()-> new IllegalStateException("조회할 step객체가 없습니다."));

        // 걸음수, 미션완료 여부 가져오기
        int todaySteps = step.getStepCount();
        boolean isComplete = step.isComplete();

        // TODO 리워드 지급 함수 분리하기
        if (todaySteps >= 7000) {
            if (isComplete) {
                return Response.fail("이미 지급 완료되었습니다.");
            }
            // 리워드 지급
            Optional<MemberInfo> memberInfoOptional = memberInfoRepository.findByUserId(member);
            MemberInfo memberInfo = memberInfoOptional.orElseThrow(()->new IllegalArgumentException("조회할 MemberInfo가 없습니다."));
            memberInfo.addReward(3);
            memberInfoRepository.save(memberInfo);

            // 미션완료 변경
            step.updateisComplete();

            return Response.success("리워드 지급 완료", 3);

        }

        return Response.fail("걸음수 부족");
    }

    // 오늘 걸음수
    private int getTodayStepCount(Member member) {
        LocalDate today = LocalDate.now();
        Optional<Step> stepOptional = stepRepository.findByUserIdAndStepDate(member, today);
        Step step = stepOptional.orElseThrow(()-> new IllegalStateException("step객체가 없습니다."));
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
        // TODO 버스, 전철, 차량 폭이 너무큰데 어떤거할지?
        double temp = weeklyStepCount * 0.75 / 1000;    // km로 환산
        double carbonReductionCar = temp * 0.192;       // 가솔린차량
        double carbonReductionBus = temp * 0.105;       // 버스
        double carbonReductionSubway = temp * 0.052;    // 전철

        return carbonReductionBus;
    }

}
