package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DrawResponseDto {
    private Long characterId;
    private String characterName;
    private int characterRarity;
    private String characterImageUrl;
}