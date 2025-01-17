package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.WeekReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {

    @Query("SELECT wr FROM WeekReport wr WHERE wr.userId = :user AND wr.weekStartDate = :startDate AND wr.weekEndDate = :endDate")
    Optional<WeekReport> findByUserAndWeekRange(
            @Param("user") Member user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
