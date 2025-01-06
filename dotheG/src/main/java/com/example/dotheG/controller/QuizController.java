package com.example.dotheG.controller;

import com.example.dotheG.dto.QuizDto;
import com.example.dotheG.dto.QuizRequestDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.QuizService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // 오늘의 퀴즈 이미 풀었는지 조회
    @GetMapping()
    public Response<Object> check(@RequestBody QuizRequestDto quizRequestDto){
        quizService.check(quizRequestDto.getUserId(), quizRequestDto.getQuizId());
        return Response.success("퀴즈 조회", quizService.check(quizRequestDto.getUserId(), quizRequestDto.getQuizId()));
    }

    // 퀴즈 풀기
    @PostMapping("/solve")
    public Response<Object> solve(@RequestBody QuizDto quizDto){
        quizService.solve(quizDto.getUserId(), quizDto.getQuizId(), quizDto.getMyAnswer());
        return Response.success("걸음수 업데이트", null);
    }
}
