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

    private int todayStep;  // 오늘 걸음수 (매일 초기화)

    private int weeklyStep; // 주간 걸음수 (매주 초기화)

    private int totalStep;  // 누적 걸음수

    @Column(columnDefinition = "boolean default false")
    private boolean todayMissionComplete;   // 일일 걸음수 달성 여부

    @Column(columnDefinition = "boolean default false")
    private boolean weeklyMissionComplete;  // 주간 걸음수 달성 여부

    private double thisMonthSavedCarbon; // 이번 달 탄소 절감량

    private double lastMonthSavedCarbon; // 지난 달 탄소 절감량

    private double totalSavedCarbon; // 누적 탄소 절감량

    public Step(Member userId) {
        this.userId = userId;
        this.todayStep = 0;
        this.weeklyStep = 0;
        this.totalStep = 0;
    }

    public void updateStep(int walkingCount) {
        this.weeklyStep += walkingCount-todayStep;
        this.totalStep += walkingCount-todayStep;
        this.todayStep = walkingCount;
    }

    public void resetTodayStep() {
        this.todayStep = 0;
        this.todayMissionComplete = false;
    }

    public void resetWeeklyStep() {
        this.weeklyStep = 0;
        this.weeklyMissionComplete = false;
    }

    public void setTodayMissionComplete() {
        this.todayMissionComplete = true;
    }

    public void setWeeklyMissionComplete() {
        this.weeklyMissionComplete = true;
    }

}
