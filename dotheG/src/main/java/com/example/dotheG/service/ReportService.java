package com.example.dotheG.service;

import com.example.dotheG.dto.WeeklyReportResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberActivity;
import com.example.dotheG.model.Step;
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
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
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
}
