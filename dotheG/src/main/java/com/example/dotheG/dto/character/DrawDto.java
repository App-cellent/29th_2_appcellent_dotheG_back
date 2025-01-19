package com.example.dotheG.dto.character;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DrawDto {
    private Long userId;
    private DrawType drawType;
    private String animalName;
}