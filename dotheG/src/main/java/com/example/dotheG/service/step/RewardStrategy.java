package com.example.dotheG.service.step;

import com.example.dotheG.model.Step;

public interface RewardStrategy {
    int getRequiredSteps();                 // 요구걸음수
    int getRewardPoints();                  // 지급될 리워드
    boolean isRewardGiven(Step step);       // 보상 지급여부
    void setRewardGiven(Step step);         // 보상 처리
    int getCurrentSteps(Step step);         // 현재 걸음수
}
