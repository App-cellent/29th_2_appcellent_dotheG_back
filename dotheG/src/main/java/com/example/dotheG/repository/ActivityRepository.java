package com.example.dotheG.repository;

import com.example.dotheG.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    // 오늘의 퀘스트
    @Query(value = """
        SELECT activity
        FROM Activity activity
        WHERE activity.activityId BETWEEN :startId AND :endId
        ORDER BY RAND()
        LIMIT 1
    """)
    Activity findRandomActivity(@Param("startId") int startId, @Param("endId") int endId);
}
