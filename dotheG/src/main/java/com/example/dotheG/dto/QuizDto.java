package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizDto {
    private String myAnswer;
    private Long userId;
    private Long quizId;
}
