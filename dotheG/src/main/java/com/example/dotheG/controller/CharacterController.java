package com.example.dotheG.controller;

import com.example.dotheG.dto.*;
import com.example.dotheG.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    // 캐릭터 뽑기
    @PostMapping("/draw/{userId}")
    public Response<DrawResponseDto> drawCharacter(@PathVariable("userId") Long userId, @RequestBody DrawDto drawDto) {
        DrawResponseDto responseDto = characterService.drawCharacter(drawDto, userId);
        return Response.success("캐릭터 뽑기 완료", responseDto);
    }

    // 캐릭터 도감 조회
    @GetMapping("/collection")
    public Response<List<CharacterDto>> getCharacterCollection(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String viewType
    ) {
        List<CharacterDto> characters = characterService.getCharacterCollection(userId, viewType);
        return Response.success("캐릭터 도감 조회 성공", characters);
    }

    // 캐릭터 상세 보기
    @GetMapping("/detail/{characterId}")
    public Response<CharacterDto> getCharacterDetail(@PathVariable Long characterId) {
        CharacterDto characterDto = characterService.getCharacterDetail(characterId);
        return Response.success("캐릭터 상세 조회 성공", characterDto);
    }

    // 대표 캐릭터 지정
    @PostMapping("/main/set")
    public Response<String> setMainCharacter(@RequestBody SetMainCharacterDto request) {
        String message = characterService.setMainCharacter(request.getUserId(), request.getCharacterId());
        return Response.success(message, null);
    }

    // 대표 캐릭터 조회
    @GetMapping("/main")
    public Response<MainCharacterResponseDto> getMainCharacter(@RequestParam Long userId) {
        MainCharacterResponseDto mainCharacter = characterService.getMainCharacter(userId);
        return Response.success("대표 캐릭터 조회 성공", mainCharacter);
    }

}