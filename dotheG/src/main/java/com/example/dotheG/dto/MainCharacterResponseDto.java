package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainCharacterResponseDto {
    private Long characterId;
    private String characterName;
    private int characterRarity;
    private String characterImageUrl;
    private int userReward;
}
