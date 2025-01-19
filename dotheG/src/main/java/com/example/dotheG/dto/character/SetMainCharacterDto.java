package com.example.dotheG.dto.character;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SetMainCharacterDto {
    private Long userId;
    private Long characterId;
}