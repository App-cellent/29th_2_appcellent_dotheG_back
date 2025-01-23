package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainpageResponseDto {
    private String userName;
    private int userReward;
    private Long mainChar;
    private double monthSavedTree;
    private double totalSavedTree;
    private Long dailyActivity;
    private Long specialActivity;
}
