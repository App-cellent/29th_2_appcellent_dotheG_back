package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.WeekReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    @Query("SELECT COUNT(w) > 0 FROM WeekReport w WHERE w.userId = :user AND w.weekStartDate = :startDate AND w.weekEndDate = :endDate")
    boolean existsByUserAndWeekRange(
            @Param("user") Member user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = "SELECT * FROM WEEK_REPORT WHERE USER_ID = :userId ORDER BY WEEK_REPORT_ID DESC LIMIT 1", nativeQuery = true)
    Optional<WeekReport> findLatestReportByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(w.weeklyAvgSteps), 0) * 7 " +
            "FROM WeekReport w " +
            "WHERE w.userId.userId = :userId " +
            "AND w.weekStartDate >= :startDate " +
            "AND w.weekEndDate <= :endDate")
    int findStepsByUserInRange(@Param("userId") Long userId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);

}