package com.example.dotheG.service.step;

import com.example.dotheG.model.Step;

public class TodayStepRewardStrategy implements RewardStrategy {
    @Override
    public int getRequiredSteps() {
        return 7000;
    }

    @Override
    public int getRewardPoints() {
        return 3;
    }

    @Override
    public boolean isRewardGiven(Step step) {
        return step.isTodayMissionComplete();
    }

    @Override
    public void setRewardGiven(Step step) {
        step.setTodayMissionComplete();
    }

    @Override
    public int getCurrentSteps(Step step) {
        return step.getTodayStep();
    }
}
