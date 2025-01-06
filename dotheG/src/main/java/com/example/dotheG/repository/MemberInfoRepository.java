package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
    MemberInfo findByUserId(Member member);
}
