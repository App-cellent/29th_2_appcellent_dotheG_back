package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class WeeklyReportResponseDto {
    private String userName; // 사용자 이름
    private String yearMonthWeek; // 몇 년 몇 월 몇째 주
    private int weeklyAvgSteps; // 주간 평균 걸음수
    private int totalCertifications; // 총 인증 횟수
    private Map<String, Long> activityCounts; // 활동별 인증 횟수
}
