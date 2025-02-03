package com.example.dotheG.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MonthlyReportResponseDto {
    private String userName; // 사용자 이름
    private String reportMonth; // 몇 년 몇 월
    private double treesSaved; // 탄소 절감량
    private int monthlyTotalCertifications;    // 총 인증 횟수
    private Map<String, Long> activityCounts;  // 세부 인증 횟수
    private int userPercentage; // 사용자 탄소 절감량 상위 %
    private String userRange; // 사용자 탄소 절감량 Range
}