package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {
    List<MemberActivity> findByUserIdAndActivityDate(Member member, LocalDate activityDate);
}
