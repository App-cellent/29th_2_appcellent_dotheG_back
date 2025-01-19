package com.example.dotheG.service;

import com.example.dotheG.dto.report.MonthlyReportResponseDto;
import com.example.dotheG.dto.report.WeeklyReportResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.*;
import com.example.dotheG.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberService memberService;
    private final WeekReportRepository weekReportRepository;
    private final MemberActivityRepository memberActivityRepository;
    private final StepRepository stepRepository;
    private final MonthReportRepository monthReportRepository;

    // 주간 보고서 저장
    @Transactional
    @Scheduled(cron = "30 23 * * 7 *") // 매주 일요일 23:30 실행
    public void saveWeeklyReport() {
        // 사용자 정보 조회
        Member member = memberService.getCurrentMember();
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 현재 날짜 기준으로 전 주 월요일과 일요일 계산
        LocalDate lastWeekMonday = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastWeekSunday = lastWeekMonday.plusDays(6);

        // 동일한 사용자와 기간의 주간 보고서가 이미 존재하는지 확인
        boolean exists = weekReportRepository.existsByUserAndWeekRange(member, lastWeekMonday, lastWeekSunday);
        if (exists) {
            return; // 이미 주간 보고서가 존재하면 저장하지 않음
        }

        // Step 테이블에서 주간 걸음수 가져오기
        Step step = stepRepository.findByUserId(member)
                .orElseThrow(() -> new CustomException(ErrorCode.STEP_NOT_FOUND));
        int weeklyStepCount = step.getWeeklyStep(); // 주간 걸음수 가져오기
        int dailyAverageSteps = weeklyStepCount / 7; // 주간 평균 걸음수

        // 활동 인증 데이터 조회
        List<MemberActivity> activities = memberActivityRepository.findActivitiesByUserAndDateRange(
                member, lastWeekMonday, lastWeekSunday
        );
        int weeklyCertification = activities.size();

        // 주간 보고서 저장
        WeekReport weekReport = new WeekReport(
                null,
                member,
                weeklyCertification,
                dailyAverageSteps,
                lastWeekMonday,
                lastWeekSunday
        );

        weekReportRepository.save(weekReport);
    }

    // 주간 보고서 조회
    @Transactional(readOnly = true)
    public WeeklyReportResponseDto getWeeklyReport() {
        // 사용자 정보 조회
        Member member = memberService.getCurrentMember();
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 가장 최신 주간 보고서 조회
        WeekReport weekReport = weekReportRepository.findLatestReportByUser(member.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        // 활동 인증 데이터 조회
        List<MemberActivity> activities = memberActivityRepository.findActivitiesByUserAndDateRange(
                member, weekReport.getWeekStartDate(), weekReport.getWeekEndDate()
        );

        Map<String, Long> activityCounts = activities.stream()
                .collect(Collectors.groupingBy(MemberActivity::getActivityName, Collectors.counting()));

        int totalCertifications = activityCounts.values().stream().mapToInt(Long::intValue).sum();

        return new WeeklyReportResponseDto(
                weekReport.getWeeklyAvgSteps(),
                totalCertifications,
                activityCounts
        );
    }

    // 월간 보고서 저장
    @Transactional
    @Scheduled(cron = "0 30 23 28-31 * ?", zone = "Asia/Seoul") // 매달 마지막 날 23:30 실행
    public void saveMonthlyReport() {
        // 사용자 정보 조회
        Member member = memberService.getCurrentMember();
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 지난 달 계산
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        // 지난 달 총 걸음 수 계산
        int monthlyTotalSteps = weekReportRepository.findStepsByUserInRange(member.getUserId(), firstDayOfLastMonth, lastDayOfLastMonth);

        // 활동 인증 데이터 조회
        List<MemberActivity> activities = memberActivityRepository.findActivitiesByUserAndDateRange(
                member, firstDayOfLastMonth, lastDayOfLastMonth
        );
        int monthlyTotalCertifications = activities.size();

        // 월간 보고서 저장
        MonthReport monthReport = new MonthReport(
                member,
                firstDayOfLastMonth,
                monthlyTotalSteps,
                monthlyTotalCertifications
        );

        monthReportRepository.save(monthReport);
    }

    // 월간 보고서 조회
    @Transactional(readOnly = true)
    public MonthlyReportResponseDto getMonthlyReport() {
        // 사용자 정보 조회
        Member member = memberService.getCurrentMember();
        if (member == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 지난 달의 시작일과 종료일 계산
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        // 월간 보고서 조회
        MonthReport monthReport = monthReportRepository.findByUserIdAndReportMonth(member, firstDayOfLastMonth)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));

        // 인증 데이터 조회
        List<MemberActivity> activities = memberActivityRepository.findActivitiesByUserAndDateRange(
                member, firstDayOfLastMonth, lastDayOfLastMonth
        );

        // 인증 히스토리 계산
        Map<String, Long> activityCounts = activities.stream()
                .collect(Collectors.groupingBy(MemberActivity::getActivityName, Collectors.counting()));

        // 총 인증 횟수 계산
        int totalCertifications = activityCounts.values().stream().mapToInt(Long::intValue).sum();

        // 지난 달 걸음수
        int monthlyTotalSteps = monthReport.getMonthlyTotalSteps();

        // 탄소 절감량 계산 (1000걸음당 150g)
        double carbonReduction = (monthlyTotalSteps / 1000.0) * 150;

        // 지킨 나무 수 계산 (탄소 절감량 / 22000g)
        double treesSaved = Math.round((carbonReduction / 22000) * 100.0) / 100.0;

        // MonthlyReportResponseDto 반환
        return new MonthlyReportResponseDto(
                firstDayOfLastMonth.toString(), // 보고서 월
                treesSaved,                    // 지킨 나무 수
                totalCertifications,           // 총 인증 횟수
                activityCounts,                // 각 인증별 횟수
                null                           // 탄소 배출량 순위는 추후 구현
        );
    }
}