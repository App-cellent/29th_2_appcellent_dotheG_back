package com.example.dotheG.dto.character;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainCharacterResponseDto {
    private Long characterId;
    private String characterName;
    private Integer characterRarity;
    private String characterImageUrl;
    private int userReward;
}
