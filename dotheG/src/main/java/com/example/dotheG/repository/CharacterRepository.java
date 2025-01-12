package com.example.dotheG.repository;

import com.example.dotheG.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CharacterRepository extends JpaRepository<Character, Long> {

    // 랜덤 뽑기 (가중치 적용)
    @Query(value = """
        SELECT *
        FROM Character
        ORDER BY CASE
            WHEN CHAR_RARITY = 1 THEN RAND() * 35
            WHEN CHAR_RARITY = 2 THEN RAND() * 30
            WHEN CHAR_RARITY = 3 THEN RAND() * 25
            WHEN CHAR_RARITY = 4 THEN RAND() * 10
        END DESC LIMIT 1
    """, nativeQuery = true)
    Character findRandomCharacterByWeightedRarity();

    // 특정 동물 뽑기
    @Query(value = """
        SELECT *
        FROM Character
        WHERE CHAR_NAME = :animalName
        ORDER BY RAND() LIMIT 1
    """, nativeQuery = true)
    Character findRandomByAnimal(@Param("animalName") String animalName);

    // 희귀도별 뽑기
    @Query(value = """
            SELECT *
            FROM Character
            WHERE CHAR_RARITY = :rarity
            ORDER BY RAND() LIMIT 1
    """, nativeQuery = true)
    Character findRandomByRarity(@Param("rarity") int rarity);
}