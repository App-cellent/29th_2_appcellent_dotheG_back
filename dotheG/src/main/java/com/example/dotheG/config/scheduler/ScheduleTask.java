package com.example.dotheG.config.scheduler;

import com.example.dotheG.service.QuizService;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduleTask {

    private final QuizService quizService;

    public ScheduleTask(QuizService quizService) {
        this.quizService = quizService;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 리셋
    public void resetDailyQuiz() {
        // Fixme : 스케줄러는 하나로 합쳐두는 게 나은 거 아닌가?
        quizService.resetDailyQuiz();
    }
}
