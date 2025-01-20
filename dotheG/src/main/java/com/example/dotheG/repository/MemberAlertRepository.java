package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAlertRepository extends JpaRepository<MemberAlert, Long> {
    List<MemberAlert> findAllByUserId(Member member);
    List<MemberAlert> findAllByUserIdOrderBySendTimeDesc(Member member);
    MemberAlert findByUserIdAndUserAlertId(Member userId, Long userAlertId);
}
