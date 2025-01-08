package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {
    Optional<MemberQuiz> findByUserIdAndQuizId(Member userId, Quiz quizId);
}
