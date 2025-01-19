package com.example.dotheG.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MonthlyReportResponseDto {
    private String reportMonth;                // 몇 년 몇 월
    private double treesSaved;            // 탄소 절감량
    private int monthlyTotalCertifications;    // 총 인증 횟수
    private Map<String, Long> activityCounts;  // 세부 인증 횟수
    private String carbonEmissionRank;         // 나의 탄소 배출량 순위
}