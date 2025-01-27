package com.example.dotheG.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuizResponseDto {
    private Long quizType;
    private String quizTitle;
    private List<String> quizText;
}
