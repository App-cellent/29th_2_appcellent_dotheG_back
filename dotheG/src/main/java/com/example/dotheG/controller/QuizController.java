package com.example.dotheG.controller;

import com.example.dotheG.dto.QuizResponseDto;
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
    public Response<Object> check(){
        return Response.success("퀴즈 조회", quizService.check());
        // 풀었으면 true 안풀었으면 false
    }

    // 퀴즈 정보 전달
    @GetMapping("/getQuiz")
    public Response<QuizResponseDto> getQuiz(){
        return Response.success("퀴즈 불러오기", quizService.getQuiz());
    }

    // 퀴즈 풀기
    @PostMapping("/answer")
    public Response<Object> solve(@RequestBody String myAnswer){
        return Response.success("퀴즈 풀기 성공", quizService.solve(myAnswer));
    }
}
