package com.example.dotheG.repository;

import com.example.dotheG.model.Member;
import com.example.dotheG.model.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
    Optional<Withdraw> findByUserId(Member userId);


}
