package com.example.dotheG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUIZ_ID")
    private Long quizId;

    private Date quizDate;

    private String quizAnswer;

    private String quizSol;
}
