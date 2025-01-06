package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface StepRepository extends JpaRepository<Step, Long> {
    Optional<Step> findByUserIdAndStepDate(Member member, LocalDate date);

    // 주간, 월간, 누적 집계용
    @Query("select sum(s.stepCount) from Step s where s.userId = :member and s.stepDate between :startDate and :endDate")
    int findWeeklyStepsByMember(Member member, LocalDate startDate, LocalDate endDate);

    @Query("select sum(s.stepCount) from Step s where s.userId = :member and s.stepDate between :startDate and :endDate")
    int findMonthlyStepsByMember(Member member, LocalDate startDate, LocalDate endDate);

    @Query("select sum(s.stepCount) from Step s where s.userId = :member")
    int findTotalStepsByMember(Member member);
}
