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

    // 일일 걸음수 초기화(매일 자정에 작동)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void runTaskMidnightKST() {
        stepService.resetTodayStep();
    }

    // 주간 걸음수 초기화(매주 월요일 자정에 작동)
    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void runTaskMondayMidnightKST() {
        stepService.resetWeeklyStep();
    }

    // 월간 걸음수 초기화(매달 1일 자정에 작동)
    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Seoul")
    public void runTaskFirstDayMidnightKST() {
        stepService.resetMonthlyStep();
    }

}
