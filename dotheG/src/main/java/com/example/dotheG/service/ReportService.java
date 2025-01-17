package com.example.dotheG.service;

import com.example.dotheG.dto.WeeklyReportResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberActivity;
import com.example.dotheG.model.WeekReport;
import com.example.dotheG.repository.MemberActivityRepository;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.StepRepository;
import com.example.dotheG.repository.WeekReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberService memberService;
    private final WeekReportRepository weekReportRepository;
    private final MemberActivityRepository memberActivityRepository;
    private final StepRepository stepRepository;

    // 주간 보고서 저장
    @Transactional
    @Scheduled(cron = "30 23 * * 7 *") // 매주 일요일 23:30 실행
    public void saveWeeklyReport() {
        // 현재 날짜 기준으로 전 주 월요일과 일요일 계산
        LocalDate today = LocalDate.now();
        LocalDate lastWeekMonday = today.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastWeekSunday = lastWeekMonday.plusDays(6);

        // 사용자 정보 조회
        Member member = memberService.getCurrentMember();

        // 지난 주 걸음수 데이터 조회
        int weeklyStepCount = stepRepository.getWeeklySteps(member, lastWeekMonday, lastWeekSunday);
        int dailyAverageSteps = weeklyStepCount / 7;

        // 지난 주 활동 인증 데이터 조회
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
}
