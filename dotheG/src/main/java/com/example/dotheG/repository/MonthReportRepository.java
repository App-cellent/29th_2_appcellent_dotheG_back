package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MonthReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthReportRepository extends JpaRepository<MonthReport, Long> {
    @Query("SELECT m FROM MonthReport m WHERE m.userId = :userId AND m.reportMonth = :reportMonth")
    Optional<MonthReport> findByUserIdAndReportMonth(@Param("userId") Member userId,
                                                     @Param("reportMonth") LocalDate reportMonth);

    // 특정 기간에 해당하는 월간 보고서 조회
    @Query("SELECT m FROM MonthReport m WHERE m.reportMonth BETWEEN :startMonth AND :endMonth")
    List<MonthReport> findByReportMonthBetween(@Param("startMonth") LocalDate startMonth,
                                               @Param("endMonth") LocalDate endMonth);


}
