package com.example.dotheG.config.scheduler;

import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleTask {

    private MemberQuizRepository memberQuizRepository;
    private final ReportService reportService;

    // isSolved -> false
    // isCorrect -> null
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 리셋
    public void resetDailyQuiz(){
        List<MemberQuiz> quizzes = memberQuizRepository.findAll();
        List<MemberQuiz> updateQuiz = quizzes.stream()
                .map(quiz -> new MemberQuiz(quiz.getUserId(), quiz.getQuizId()))
                .toList();
        memberQuizRepository.saveAll(updateQuiz);
    }

    @Scheduled(cron = "30 23 * * 7 *") // 매주 일요일 23:30 실행
    public void saveWeeklyReport() {
        reportService.saveWeeklyReport();
    }
}
