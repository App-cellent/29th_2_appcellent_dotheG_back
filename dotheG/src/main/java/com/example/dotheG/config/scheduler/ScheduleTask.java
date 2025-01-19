package com.example.dotheG.config.scheduler;

import com.example.dotheG.service.QuizService;
import com.example.dotheG.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {

    private final QuizService quizService;
    private final ReportService reportService;

    public ScheduleTask(QuizService quizService, ReportService reportService) {
        this.quizService = quizService;
        this.reportService = reportService;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 리셋
    public void resetDailyQuiz() {
        // Fixme : 스케줄러는 하나로 합쳐두는 게 나은 거 아닌가?
        quizService.resetDailyQuiz();
    }

    @Scheduled(cron = "30 23 * * 7 *") // 매주 일요일 23:30 실행
    public void saveWeeklyReport() {
        reportService.saveWeeklyReport();
    }

    @Scheduled(cron = "0 30 23 28-31 * ?", zone = "Asia/Seoul") // 매달 마지막 날 23:30 실행
    public void saveMonthlyReport() { reportService.saveMonthlyReport();}
}
