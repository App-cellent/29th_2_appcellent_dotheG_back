package com.example.dotheG.controller;

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
    public Response<Object> check(@RequestParam("quizId") Long quizId){
        return Response.success("퀴즈 조회", quizService.check(quizId));
    }

    // 퀴즈 풀기
    @PostMapping("/{quizId}/answer")
    public Response<Object> solve(@PathVariable Long quizId, @RequestBody String myAnswer){
        return Response.success("퀴즈 풀기 성공", quizService.solve(quizId, myAnswer));
    }
}
