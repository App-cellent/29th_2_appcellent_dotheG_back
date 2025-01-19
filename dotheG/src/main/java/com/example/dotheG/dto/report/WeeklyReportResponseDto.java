package com.example.dotheG.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class WeeklyReportResponseDto {
    private int weeklyAvgSteps; // 하루 평균 걸음수
    private int totalCertifications; // 총 인증 횟수
    private Map<String, Long> activityCounts; // 활동별 인증 횟수
}
