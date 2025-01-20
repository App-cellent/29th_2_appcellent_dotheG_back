package com.example.dotheG.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
    private Long quizType;
    private String quizTitle;
    private List<String> quizText;
}
