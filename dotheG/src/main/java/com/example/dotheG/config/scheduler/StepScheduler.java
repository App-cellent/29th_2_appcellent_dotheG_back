package com.example.dotheG.config.scheduler;

import com.example.dotheG.service.StepService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StepScheduler {
    @Autowired
    private final StepService stepService;

    @Scheduled(cron = "0 0 0 * * *")
    public void createDailySteps() {
        stepService.createStepForAllUsers();
    }
}
