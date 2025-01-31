package com.example.dotheG.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyCarbonRankingDto {
    private String userName;            // 사용자의 이름
    private double userPercentage;      // 사용자가 상위 몇 %
    private String userRange;           // 사용자가 속한 range
}
