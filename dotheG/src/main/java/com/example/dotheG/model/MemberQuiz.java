package com.example.dotheG.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
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

    @Column(columnDefinition = "boolean default false")
    private boolean isSolved;

    @Nullable
    private Boolean isCorrect;

    public void updateStatus(Quiz quizId, boolean isCorrect, boolean isSolved) {
        this.quizId = quizId;
        this.isCorrect = isCorrect;
        this.isSolved = isSolved;
    }

    public MemberQuiz(Member userId){
        this.userId = userId;
        this.quizId = null;
        this.isSolved = false;
        this.isCorrect = null;
    }

    public void resetMemberQuiz(){
        this.quizId = null;
        this.isSolved = false;
        this.isCorrect = null;
    }
}
