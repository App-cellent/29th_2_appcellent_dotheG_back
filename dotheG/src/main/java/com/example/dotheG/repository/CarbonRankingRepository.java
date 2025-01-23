package com.example.dotheG.repository;

import com.example.dotheG.model.CarbonRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarbonRankingRepository extends JpaRepository<CarbonRanking, Long> {
    List<CarbonRanking> findAll();

    @Modifying
    @Query("UPDATE CarbonRanking c SET c.userCount = 0")
    void resetUserCounts();

    @Modifying
    @Query("UPDATE CarbonRanking c SET c.userCount = c.userCount + 1 WHERE c.range = :range")
    void incrementUserCountByRange(@Param("range") String range);
}