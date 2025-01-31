package com.example.dotheG.service;

import com.example.dotheG.dto.report.CarbonRankingDto;
import com.example.dotheG.dto.report.MonthlyReportResponseDto;
import com.example.dotheG.dto.report.WeeklyReportResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.*;
import com.example.dotheG.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Comparator;
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
    private final CarbonRankingRepository carbonRankingRepository;

    // 주간 보고서 저장
    @Transactional // 매주 일요일 23:30 실행
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
        int count = weekReportRepository.countByUserAndWeekRange(member.getUserId(), lastWeekMonday, lastWeekSunday);
        if (count > 0) {
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
        // 현재 시간이 제한 시간 내인지 확인
        checkWeeklyReportTimeRestriction();

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

        // 년/월/주 계산
        LocalDate startDate = weekReport.getWeekStartDate();
        int year = startDate.getYear();
        int month = startDate.getMonthValue();
        int week = startDate.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

        String yearMonthWeek = String.format("%d년 %d월 %s", year, month, getKoreanWeekString(week));

        // WeeklyReportResponseDto 생성 및 반환
        return new WeeklyReportResponseDto(
                member.getUserName(),      // 사용자 이름
                yearMonthWeek,             // 년/월/주 정보
                weekReport.getWeeklyAvgSteps(),
                totalCertifications,
                activityCounts
        );
    }

    // 주차를 한글로 변환
    private String getKoreanWeekString(int week) {
        switch (week) {
            case 1: return "첫째 주";
            case 2: return "둘째 주";
            case 3: return "셋째 주";
            case 4: return "넷째 주";
            case 5: return "다섯째 주";
            default: return week + "째 주";
        }
    }

    // 월간 보고서 저장 (매달 마지막 날 23:30)
    @Transactional
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

        // Step 테이블에서 지난 달 월간 걸음수 가져오기
        Step step = stepRepository.findByUserId(member)
                .orElseThrow(() -> new CustomException(ErrorCode.STEP_NOT_FOUND));
        int monthlyTotalSteps = step.getMonthlyStep(); // 월간 걸음수 가져오기

        // 활동 인증 데이터 조회
        List<MemberActivity> activities = memberActivityRepository.findActivitiesByUserAndDateRange(
                member, firstDayOfLastMonth, lastDayOfLastMonth
        );
        int monthlyTotalCertifications = activities.size();

        MonthReport monthReport = new MonthReport(
                member,
                firstDayOfLastMonth,
                monthlyTotalSteps,
                monthlyTotalCertifications,
                0, // 초기값: 0
                null // 초기값: null
        );

        monthReportRepository.save(monthReport);
    }

    // 월간 보고서 userPercentage, userRange 업데이트 (매달 마지막 날 23:50)
    @Transactional
    public void updateMonthlyReports() {
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        // 1. 데이터 로드
        List<MonthReport> reports = monthReportRepository.findByReportMonthBetween(firstDayOfLastMonth, lastDayOfLastMonth);
        System.out.println("Loaded Reports: " + reports.size());

        if (reports.isEmpty()) {
            System.out.println("No reports found for the given month range.");
            return; // 데이터가 없으면 종료
        }

        // 2. 모든 사용자 탄소 절감량 계산 및 정렬
        List<Double> reductions = reports.stream()
                .map(r -> getCarbonReduction(r.getMonthlyTotalSteps()))
                .sorted(Comparator.reverseOrder())
                .toList();
        System.out.println("Reductions List: " + reductions);

        // 3. 각 사용자 보고서 업데이트
        for (MonthReport report : reports) {
            double reduction = getCarbonReduction(report.getMonthlyTotalSteps());
            int rank = reductions.indexOf(reduction) + 1;
            int percentage = (int) Math.round((rank / (double) reductions.size()) * 100);
            String carbonRange = getRangeForCarbonReduction(reduction);

            System.out.println("Updating Report: " + report.getMonthReportId() + ", Reduction: " + reduction + ", Rank: " + rank + ", Percentage: " + percentage + ", Range: " + carbonRange);

            report.updateCarbonInfo(percentage, carbonRange);
        }

        // 4. 저장
        monthReportRepository.saveAll(reports);
        System.out.println("Reports updated successfully.");
    }

    // 월간 보고서 조회
    @Transactional(readOnly = true)
    public MonthlyReportResponseDto getMonthlyReport() {
        // 현재 시간이 제한 시간 내인지 확인
        checkMonthlyReportTimeRestriction();

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

        // 보고서 월 계산 (몇 년 몇 월)
        String reportMonth = String.format("%d년 %d월", firstDayOfLastMonth.getYear(), firstDayOfLastMonth.getMonthValue());

        // MonthlyReportResponseDto 반환
        return new MonthlyReportResponseDto(
                member.getUserName(),  // 사용자 이름
                reportMonth,           // 보고서 월
                treesSaved,            // 지킨 나무 수
                totalCertifications,   // 총 인증 횟수
                activityCounts,         // 각 인증별 횟수
                monthReport.getUserPercentage(),
                monthReport.getUserRange()
        );
    }

    // 주간 보고서 시간 제한 확인
    private void checkWeeklyReportTimeRestriction() {
        LocalTime now = LocalTime.now();
        DayOfWeek day = LocalDate.now().getDayOfWeek();

        // 매주 일요일 밤 11:30 ~ 월요일 0:00 제한
        if (day == DayOfWeek.SUNDAY && now.isAfter(LocalTime.of(23, 30)) ||
                day == DayOfWeek.MONDAY && now.isBefore(LocalTime.of(0, 0))) {
            throw new CustomException(ErrorCode.TIME_RESTRICTION_WEEKLY);
        }
    }

    // 월간 보고서 시간 제한 확인
    private void checkMonthlyReportTimeRestriction() {
        LocalTime now = LocalTime.now();
        LocalDate today = LocalDate.now();

        // 매달 마지막 날 밤 11:30 ~ 자정 제한
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
        if (today.equals(lastDayOfMonth) && now.isAfter(LocalTime.of(23, 30)) && now.isBefore(LocalTime.of(23, 59))) {
            throw new CustomException(ErrorCode.TIME_RESTRICTION_MONTHLY);
        }
    }

    // 탄소 절감량 계산 메서드
    public double getCarbonReduction(int stepCount) {
        double temp = (stepCount / 1000.0) * 150;  // 1000보당 150g 절약
        return temp / 1000.0;                      // kg로 환산
    }

    // 탄소 절감량 분포 초기화 (매달 마지막 날 23:30)
    @Transactional
    public void resetCarbonRanking() {
        carbonRankingRepository.resetUserCounts(); // 모든 userCount를 0으로 초기화
    }

    // 탄소 절감량 분포 업데이트 (매달 마지막 날 23:40)
    @Transactional
    public void updateCarbonRanking() {
        // 모든 사용자 월간 보고서 데이터 가져오기
        List<MonthReport> monthReports = monthReportRepository.findAll();

        for (MonthReport report : monthReports) {
            int steps = report.getMonthlyTotalSteps();
            double carbonReduction = getCarbonReduction(steps);

            // 탄소 절감량에 따른 range 결정
            String carbonRange = getRangeForCarbonReduction(carbonReduction);

            // 해당 range의 userCount 증가
            carbonRankingRepository.incrementUserCountByRange(carbonRange);
        }
    }

    // 탄소 절감량에 따른 range 반환
    private String getRangeForCarbonReduction(double carbonReduction) {
        if (carbonReduction < 5) return "0 ~ 5kg";
        if (carbonReduction < 10) return "5 ~ 10kg";
        if (carbonReduction < 15) return "10 ~ 15kg";
        if (carbonReduction < 20) return "15 ~ 20kg";
        if (carbonReduction < 25) return "20 ~ 25kg";
        if (carbonReduction < 30) return "25 ~ 30kg";
        if (carbonReduction < 35) return "30 ~ 35kg";
        if (carbonReduction < 40) return "35 ~ 40kg";
        if (carbonReduction < 45) return "40 ~ 45kg";
        if (carbonReduction < 50) return "45 ~ 50kg";
        return "50kg 이상";
    }

    // 탄소 배출량 분포 조회
    @Transactional(readOnly = true)
    public List<CarbonRankingDto> getCarbonRankingGraph() {
        // 고정된 range 목록 정의
        List<String> predefinedRanges = Arrays.asList(
                "0 ~ 5kg", "5 ~ 10kg", "10 ~ 15kg", "15 ~ 20kg",
                "20 ~ 25kg", "25 ~ 30kg", "30 ~ 35kg", "35 ~ 40kg",
                "40 ~ 45kg", "45 ~ 50kg", "50kg 이상"
        );

        // 데이터베이스에서 조회
        List<CarbonRanking> existingRankings = carbonRankingRepository.findAll();

        // Map으로 변환하여 기존 데이터 확인
        Map<String, Integer> rangeToCountMap = existingRankings.stream()
                .collect(Collectors.toMap(CarbonRanking::getCarbonRange, CarbonRanking::getUserCount));

        // 모든 구간을 포함하도록 데이터 생성
        List<CarbonRankingDto> result = predefinedRanges.stream()
                .map(range -> new CarbonRankingDto(
                        range,
                        rangeToCountMap.getOrDefault(range, 0) // 값이 없으면 0으로 설정
                ))
                .collect(Collectors.toList());

        return result;
    }

}