package com.example.dotheG.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ActivityListResponse {
    private String userName; // 사용자 아이디
    private int listSize; // 리스트 길이
    private List<ActivityResponseDto> activities; // 현재 리스트
}
