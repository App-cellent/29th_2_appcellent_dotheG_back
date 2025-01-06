package com.example.dotheG.model;

import jakarta.persistence.*;

@Entity
public class MemberQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userQuizId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    @ManyToOne
    @JoinColumn(name = "QUIZ_ID")
    private Quiz quizId;

    // Todo : default value False
    private boolean isSolved;
}
