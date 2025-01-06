package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    MemberInfo findByUserId(Member member);
    Optional<MemberInfo> findByUserId(Member userId);
}

