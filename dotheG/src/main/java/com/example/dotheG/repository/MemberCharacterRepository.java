package com.example.dotheG.repository;

import com.example.dotheG.model.Character;
import com.example.dotheG.model.MemberCharacter;
import com.example.dotheG.model.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberCharacterRepository extends JpaRepository<MemberCharacter, Long> {

    // 특정 사용자의 모든 캐릭터 조회
    @Query("SELECT mc FROM MemberCharacter mc WHERE mc.userInfoId.userInfoId = :userId")
    List<MemberCharacter> findByUserInfoId_UserId(@Param("userId") Long userId);

    // 특정 사용자가 특정 캐릭터 보유했는지 확인
    boolean existsByUserInfoIdAndCharId(MemberInfo userInfoId, Character charId);
}