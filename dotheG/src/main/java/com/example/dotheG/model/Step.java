package com.example.dotheG.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STEP_ID")
    private Long stepId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Member userId;

    private int stepCount;

    @Column(columnDefinition = "boolean default false")
    private boolean isComplete;

    private LocalDate stepDate;

    public Step(Member userId, LocalDate stepDate) {
        this.userId = userId;
        this.stepCount = 0;
        this.stepDate = stepDate;

    }

    public int getStepCount() {
        return stepCount;
    }

    public void updateStepCount(int steps) {
        this.stepCount += steps;
    }

    public void updateisComplete() {
        this.isComplete = true;
    }

}
