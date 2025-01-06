package com.example.dotheG.service;

import com.example.dotheG.dto.CharacterDto;
import com.example.dotheG.dto.DrawDto;
import com.example.dotheG.dto.DrawResponseDto;
import com.example.dotheG.dto.MainCharacterResponseDto;
import com.example.dotheG.model.Character;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberCharacter;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.repository.CharacterRepository;
import com.example.dotheG.repository.MemberCharacterRepository;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberCharacterRepository memberCharacterRepository;
    private final MemberRepository memberRepository;

    // 캐릭터 뽑기
    @Transactional
    public DrawResponseDto drawCharacter(DrawDto drawDto, Long userId) {
        // 1. 사용자 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        MemberInfo userInfo = memberInfoRepository.findByUserId(member)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 2. 리워드 검증 및 차감
        int cost = drawDto.getDrawType().getCost();
        if (userInfo.getUserReward() < cost) {
            throw new IllegalArgumentException("리워드가 부족합니다.");
        }

        MemberInfo updatedUserInfo = new MemberInfo(
                userInfo.getUserInfoId(),
                userInfo.getUserId(),
                userInfo.getUserReward() - cost,
                userInfo.getMainChar()
        );
        memberInfoRepository.save(updatedUserInfo);

        // 3. 캐릭터 뽑기 로직
        Character drawnCharacter;
        switch (drawDto.getDrawType()) {
            case RANDOM -> {
                drawnCharacter = characterRepository.findRandomCharacterByWeightedRarity();
            }
            case ANIMAL -> {
                drawnCharacter = characterRepository.findRandomByAnimal(drawDto.getAnimalName());
            }
            case RANK_1 -> {
                drawnCharacter = characterRepository.findRandomByRarity(1);
            }
            case RANK_2 -> {
                drawnCharacter = characterRepository.findRandomByRarity(2);
            }
            case RANK_3 -> {
                drawnCharacter = characterRepository.findRandomByRarity(3);
            }
            case RANK_4 -> {
                drawnCharacter = characterRepository.findRandomByRarity(4);
            }
            default -> throw new IllegalArgumentException("잘못된 뽑기 유형입니다.");
        }

        // 4. 중복 여부 확인 및 저장
        if (!memberCharacterRepository.existsByUserInfoIdAndCharId(updatedUserInfo, drawnCharacter)) {
            MemberCharacter memberCharacter = new MemberCharacter(updatedUserInfo, drawnCharacter);
            memberCharacterRepository.save(memberCharacter);
        }

        // 5. DrawResponseDto 생성 및 반환
        return new DrawResponseDto(
                drawnCharacter.getCharId(),
                drawnCharacter.getCharName(),
                drawnCharacter.getCharRarity(),
                drawnCharacter.getCharImageUrl()
        );
    }

    // 캐릭터 도감 조회
    public List<CharacterDto> getCharacterCollection(Long userId, String viewType) {
        // 1. 사용자 보유 캐릭터 조회
        List<MemberCharacter> ownedCharacters = memberCharacterRepository.findByUserInfoId_UserId(userId);

        // 2. 필터링 조건 설정
        return ownedCharacters.stream()
                .filter(memberCharacter -> filterByViewType(memberCharacter.getCharId().getCharRarity(), viewType))
                .map(memberCharacter -> new CharacterDto(
                        memberCharacter.getCharId().getCharId(),
                        memberCharacter.getCharId().getCharName(),
                        memberCharacter.getCharId().getCharRarity(),
                        memberCharacter.getCharId().getCharImageUrl()
                ))
                .collect(Collectors.toList());
    }

    private boolean filterByViewType(int rarity, String viewType) {
        return switch (viewType.toUpperCase()) {
            case "ALL" -> true;
            case "COMMON" -> rarity == 1;
            case "NORMAL" -> rarity == 2;
            case "RARE" -> rarity == 3;
            case "LEGENDARY" -> rarity == 4;
            default -> false;
        };
    }

    // 캐릭터 상세 보기
    public CharacterDto getCharacterDetail(Long characterId) {
        // 캐릭터 ID로 캐릭터 정보 조회
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 캐릭터입니다."));

        // DTO로 변환하여 반환
        return new CharacterDto(
                character.getCharId(),
                character.getCharName(),
                character.getCharRarity(),
                character.getCharImageUrl()
        );
    }

    // 대표 캐릭터 지정
    @Transactional
    public String setMainCharacter(Long userId, Long characterId) {
        // 1. 사용자 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        MemberInfo memberInfo = memberInfoRepository.findByUserId(member)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 2. 캐릭터 객체 조회
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("캐릭터가 존재하지 않습니다."));

        // 3. 사용자 보유 캐릭터인지 확인
        boolean ownsCharacter = memberCharacterRepository.existsByUserInfoIdAndCharId(memberInfo, character);
        if (!ownsCharacter) {
            throw new IllegalArgumentException("사용자가 해당 캐릭터를 소유하지 않습니다.");
        }

        // 4. 대표 캐릭터 설정
        memberInfo.updateMainChar(characterId);
        memberInfoRepository.save(memberInfo);

        return "대표 캐릭터 지정 완료";
    }

    // 대표 캐릭터 조회
    @Transactional
    public MainCharacterResponseDto getMainCharacter(Long userId) {
        // 1. 사용자 정보 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        MemberInfo memberInfo = memberInfoRepository.findByUserId(member)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 2. 대표 캐릭터 ID 확인
        Long mainCharId = memberInfo.getMainChar();
        if (mainCharId == null) {
            throw new IllegalArgumentException("대표 캐릭터가 지정되지 않았습니다.");
        }

        // 3. 대표 캐릭터 정보 조회
        Character mainCharacter = characterRepository.findById(mainCharId)
                .orElseThrow(() -> new IllegalArgumentException("대표 캐릭터 정보가 존재하지 않습니다."));

        // 4. MainCharacterResponseDto 생성 및 반환
        return new MainCharacterResponseDto(
                mainCharacter.getCharId(),
                mainCharacter.getCharName(),
                mainCharacter.getCharRarity(),
                mainCharacter.getCharImageUrl(),
                memberInfo.getUserReward()
        );
    }
}