package com.example.dotheG.dto.character;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CharacterDto {
    private Long charId;
    private String charName;
    private int charRarity;
    private String charImageUrl;
    private boolean owned; // 소유 여부
}