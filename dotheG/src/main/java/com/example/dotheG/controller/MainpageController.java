package com.example.dotheG.controller;

import com.example.dotheG.dto.MainpageResponseDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.MainpageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mainpage")
public class MainpageController {
    private final MainpageService mainpageService;

    public MainpageController(MainpageService mainpageService) {
        this.mainpageService = mainpageService;
    }

    @GetMapping("/getInfo")
    public Response<MainpageResponseDto> getInfo(){
        return Response.success("메인페이지 불러오기 성공", mainpageService.getInfo());
    }

    @PostMapping("/tutorial")
    public Response<?> changeTutorial(){
        mainpageService.changeTutorial();
        return Response.success("튜토리얼 진행 했습니다.", null);
    }

}
