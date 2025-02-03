package com.example.dotheG.service;

import com.example.dotheG.dto.quiz.QuizResponseDto;
import com.example.dotheG.dto.quiz.QuizSolDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.model.Quiz;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final MemberService memberService;

    // 매일 자정 퀴즈 객체 리셋
    // isSolved -> false
    // quizId, isCorrect -> null
    public void resetDailyQuiz(){
        List<MemberQuiz> quizzes = memberQuizRepository.findAll();
        for (MemberQuiz memberQuiz : quizzes){
            memberQuiz.resetMemberQuiz();
        }
        memberQuizRepository.saveAll(quizzes);
    }

    // 오늘의 퀴즈 이미 풀었는지 조회
    public boolean check(){
        Member member = memberService.getCurrentMember();
        Long memberId = member.getUserId();
        MemberQuiz memberQuiz = memberQuizRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.TABLE_NOT_FOUND));

        // 오늘 퀴즈를 풀었는지 확인
        return memberQuiz.isSolved(); // 풀었으면 true 안풀었으면 false
    }

    // 퀴즈 불러오기
    public QuizResponseDto getQuiz() {
        // 퀴즈 조회
        Quiz quiz = quizRepository.findByQuizDate(LocalDate.now()).
                orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));

        return new QuizResponseDto(
                quiz.getQuizType(),
                quiz.getQuizTitle(),
                quiz.getQuizText()
        );
    }

    // 퀴즈 풀기
    public Object solve(String myAnswer){
        Member member = memberService.getCurrentMember();
        MemberInfo memberInfo = memberService.getCurrentMemberInfo();

        // 퀴즈 조회
        Quiz quiz = quizRepository.findByQuizDate(LocalDate.now()).orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));
        String quizAnswer = quiz.getQuizAnswer();

        QuizSolDto quizSolDto = new QuizSolDto(
                quiz.getQuizSol(),
                quiz.getQuizSolImage()
        );

        // 멤버퀴즈 조회
        Long memberId = member.getUserId();
        MemberQuiz memberQuiz = memberQuizRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.TABLE_NOT_FOUND));

        // 정답 확인 및 상태 변경 : isSolved -> true 변경
        if (myAnswer == null || myAnswer.isBlank()){
            throw new CustomException(ErrorCode.MYANSWER_NOT_FOUND);
        } else if (myAnswer.equals(quizAnswer)){
            memberInfo.addReward(2);
            memberQuiz.updateStatus(quiz, true, true);
            memberQuizRepository.save(memberQuiz);
            return "정답입니다.";
        } else {
            memberQuiz.updateStatus(quiz, false, true);
            memberQuizRepository.save(memberQuiz);
            return quizSolDto;
        }
    }
}
