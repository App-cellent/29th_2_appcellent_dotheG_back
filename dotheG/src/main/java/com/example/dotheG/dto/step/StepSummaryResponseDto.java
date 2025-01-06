package com.example.dotheG.dto.step;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StepSummaryResponseDto {
    private int today;
    private int week;
    //private int month;
    private int total;
    private double carbonReduction; // kg단위
}
