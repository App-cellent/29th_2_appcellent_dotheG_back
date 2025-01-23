package com.example.dotheG.config.scheduler;

import com.example.dotheG.service.QuizService;
import com.example.dotheG.service.ReportService;
import com.example.dotheG.service.StepService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduleTask {

    @Autowired
    private final QuizService quizService;
    @Autowired
    private final ReportService reportService;
    @Autowired
    private final StepService stepService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 매일 자정 리셋
    public void resetDailyQuiz() {
        // Fixme : 스케줄러는 하나로 합쳐두는 게 나은 거 아닌가?
        quizService.resetDailyQuiz();
    }

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

    @Scheduled(cron = "30 23 * * 7 *", zone = "Asia/Seoul") // 매주 일요일 23:30 실행
    public void saveWeeklyReport() {
        reportService.saveWeeklyReport();
    }

    @Scheduled(cron = "0 30 23 L * ?", zone = "Asia/Seoul") // 매달 마지막 날 23:30 실행
    public void saveMonthlyReport() { reportService.saveMonthlyReport();}

    @Scheduled(cron = "0 30 23 L * ?", zone = "Asia/Seoul") // 매달 마지막 날 23:30 실행
    public void resetCarbonRanking() {reportService.resetCarbonRanking();}

    @Scheduled(cron = "0 40 23 L * ?", zone = "Asia/Seoul") // 매달 마지막 날 23:40 실행
    public void updateCarbonRanking() {reportService.updateCarbonRanking();}
}
