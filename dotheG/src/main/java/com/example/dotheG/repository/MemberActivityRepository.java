package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {
    List<MemberActivity> findByUserIdAndActivityDate(Member member, LocalDate activityDate);

    @Query("SELECT ma FROM MemberActivity ma WHERE ma.userId = :user AND ma.activityDate BETWEEN :startDate AND :endDate")
    List<MemberActivity> findActivitiesByUserAndDateRange(
            @Param("user") Member user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}