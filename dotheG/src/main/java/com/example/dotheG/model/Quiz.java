package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUIZ_ID")
    private Long quizId;

    private LocalDate quizDate;

    private String quizAnswer;

    @Column(length = 1000)
    private String quizSol;

    @Column(length = 1000)
    private String quizSolImage;

    private Long quizType;

    private String quizTitle;

    @ElementCollection
    @Column(length = 1000)
    private List<String> quizText;
    
}
