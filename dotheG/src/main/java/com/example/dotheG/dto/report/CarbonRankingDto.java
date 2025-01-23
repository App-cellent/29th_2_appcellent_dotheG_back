package com.example.dotheG.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarbonRankingDto {
    private String range;    // 탄소 배출량 범위
    private int userCount;   // 범위에 해당하는 유저 수
}
