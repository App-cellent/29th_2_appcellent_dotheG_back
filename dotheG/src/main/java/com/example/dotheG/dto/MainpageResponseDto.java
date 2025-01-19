package com.example.dotheG.dto;

import com.example.dotheG.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainpageResponseDto {
    private int userReward;
    private Long mainChar;
    private double monthSavedTree;
    private double totalSavedTree;
    private Long dailyActivity;
    private Long specialActivity;
}
