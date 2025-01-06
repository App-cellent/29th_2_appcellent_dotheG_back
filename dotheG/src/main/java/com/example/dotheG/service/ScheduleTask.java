package com.example.dotheG.service;

import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.repository.MemberQuizRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ScheduleTask {

    private MemberQuizRepository memberQuizRepository;

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
}
