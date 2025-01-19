package com.example.dotheG.controller;

import com.example.dotheG.dto.MainpageResponseDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.MainpageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MainpageController {
    private final MainpageService mainpageService;

    public MainpageController(MainpageService mainpageService) {
        this.mainpageService = mainpageService;
    }

    @GetMapping("/mainpage")
    public Response<MainpageResponseDto> getInfo(){
        return Response.success("메인페이지 불러오기 성공", mainpageService.getInfo());
    }

}
