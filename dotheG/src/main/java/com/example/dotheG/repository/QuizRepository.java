package com.example.dotheG.repository;

import com.example.dotheG.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByQuizDate(LocalDate quizDate);
}
