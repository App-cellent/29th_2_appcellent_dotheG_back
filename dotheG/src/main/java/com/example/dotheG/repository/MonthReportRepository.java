package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MonthReportRepository extends JpaRepository<MonthReport, Long> {
    @Query("SELECT m FROM MonthReport m WHERE m.userId = :userId AND m.reportMonth = :reportMonth")
    Optional<MonthReport> findByUserIdAndReportMonth(@Param("userId") Member userId,
                                                     @Param("reportMonth") LocalDate reportMonth);
}
