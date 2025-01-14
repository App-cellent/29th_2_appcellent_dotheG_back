package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, Long> {
    Optional<Step> findByUserId(Member member);
}
