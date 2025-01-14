package com.example.dotheG.service.step;

import com.example.dotheG.model.Step;

public class WeeklyStepRewardStrategy implements RewardStrategy {
    @Override
    public int getRequiredSteps() {
        return 50000;
    }

    @Override
    public int getRewardPoints() {
        return 10;
    }

    @Override
    public boolean isRewardGiven(Step step) {
        return step.isWeeklyMissionComplete();
    }

    @Override
    public void setRewardGiven(Step step) {
        step.setWeeklyMissionComplete();
    }

    @Override
    public int getCurrentSteps(Step step) {
        return step.getWeeklyStep();
    }
}
