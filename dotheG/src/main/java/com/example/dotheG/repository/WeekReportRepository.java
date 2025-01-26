package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.WeekReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    @Query(value = "SELECT COUNT(*) FROM week_report WHERE user_id = :userId AND week_start_date = :startDate AND week_end_date = :endDate", nativeQuery = true)
    int countByUserAndWeekRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = "SELECT * FROM week_report WHERE user_id = :userId ORDER BY week_report_id DESC LIMIT 1", nativeQuery = true)
    Optional<WeekReport> findLatestReportByUser(@Param("userId") Long userId);
}