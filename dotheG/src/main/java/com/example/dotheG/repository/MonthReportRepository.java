package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthReportRepository extends JpaRepository<MonthReport, Long> {
    @Query(value = "SELECT * FROM MONTH_REPORT WHERE USER_ID = :userId ORDER BY MONTH_REPORT_ID DESC LIMIT 1", nativeQuery = true)
    Optional<MonthReport> findLatestReportByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) > 0 FROM MonthReport m WHERE m.userId.userId = :userId AND m.reportMonth = :reportMonth")
    boolean existsByUserIdAndReportMonth(@Param("userId") Long userId, @Param("reportMonth") LocalDate reportMonth);

    @Query("SELECT m FROM MonthReport m WHERE m.userId = :userId AND m.reportMonth = :reportMonth")
    Optional<MonthReport> findByUserIdAndReportMonth(@Param("userId") Member userId,
                                                     @Param("reportMonth") LocalDate reportMonth);
}
