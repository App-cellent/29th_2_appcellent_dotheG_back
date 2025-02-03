package com.example.dotheG.repository;

import com.example.dotheG.model.CarbonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarbonRankingRepository extends JpaRepository<CarbonRanking, Long> {
    List<CarbonRanking> findAll();

    // 모든 userCount를 0으로 초기화
    @Modifying
    @Query("UPDATE CarbonRanking c SET c.userCount = 0")
    void resetUserCounts();

    // 특정 carbonRange의 userCount를 1 증가
    @Modifying
    @Query("UPDATE CarbonRanking cr SET cr.userCount = cr.userCount + 1 WHERE cr.carbonRange = :carbonRange")
    void incrementUserCountByRange(@Param("carbonRange") String carbonRange);
}