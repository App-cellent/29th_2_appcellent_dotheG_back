package com.example.dotheG.service;

import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.model.Quiz;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTML;
import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final MemberService memberService;

    // 오늘의 퀴즈 이미 풀었는지 조회
    public boolean check(Long quizId){
        // TOdo 퀴즈 아이디 안받고 날짜만 보고 하도록 수정
        Member member = memberService.getCurrentMember();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));

        Optional<MemberQuiz> memberQuizOptional = memberQuizRepository.findByUserIdAndQuizId(member, quiz);
        MemberQuiz memberQuiz = memberQuizOptional.orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));

        // 오늘 퀴즈를 풀었는지 확인
        return memberQuiz.isSolved(); // 풀었으면 true 안풀었으면 false
    }

    // 퀴즈 풀기
    public boolean solve(Long quizId, String myAnswer){
        Member member = memberService.getCurrentMember();
        // 퀴즈 조회
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));
        String quizAnswer = quiz.getQuizAnswer();

        // 멤버퀴즈 조회
        Optional<MemberQuiz> memberQuizOptional = memberQuizRepository.findByUserIdAndQuizId(member, quiz);
        MemberQuiz memberQuiz = memberQuizOptional.orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));

        // MemberQuiz가 없으면 컬럼 생성
        if (memberQuiz == null){
            memberQuiz = new MemberQuiz(member, quiz);
        }

        // 정답 확인 및 상태 변경 : isSolved -> true 변경
        if (myAnswer == null || myAnswer.isBlank()){
            throw new CustomException(ErrorCode.MYANSWER_NOT_FOUND);
        } else {
            memberQuiz.updateStatus(myAnswer.equals(quizAnswer), true);
            memberQuizRepository.save(memberQuiz);
            return myAnswer.equals(quizAnswer);
        }
    }
}
