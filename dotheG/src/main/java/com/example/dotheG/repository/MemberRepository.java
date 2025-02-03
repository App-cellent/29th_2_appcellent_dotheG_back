package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByUserLogin(String userLogin);

    Boolean existsByUserName(String userName);

    Member findByUserLogin(String userLogin);
}

