package com.example.dotheG.service;

import com.example.dotheG.dto.QuizResponseDto;
import com.example.dotheG.dto.activity.ActivityResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.model.Quiz;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        QuizResponseDto quizResponseDto = new QuizResponseDto(
                quiz.getQuizType(),
                quiz.getQuizTitle(),
                quiz.getQuizText()
        );
        return quizResponseDto;
    }

    // 퀴즈 풀기
    public boolean solve(String myAnswer){
        Member member = memberService.getCurrentMember();
        // 퀴즈 조회
        Quiz quiz = quizRepository.findByQuizDate(LocalDate.now()).orElseThrow(() -> new CustomException(ErrorCode.QUIZ_NOT_FOUND));
        String quizAnswer = quiz.getQuizAnswer();

        // 멤버퀴즈 조회
        Long memberId = member.getUserId();
        MemberQuiz memberQuiz = memberQuizRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.TABLE_NOT_FOUND));

        // 정답 확인 및 상태 변경 : isSolved -> true 변경
        // todo : 이거 왜 null, "", " " 넣어도 true를 반환하지
        if (myAnswer == null || myAnswer.isBlank()){
            throw new CustomException(ErrorCode.MYANSWER_NOT_FOUND);
        } else {
            memberQuiz.updateStatus(quiz, myAnswer.equals(quizAnswer), true);
            memberQuizRepository.save(memberQuiz);
            return myAnswer.equals(quizAnswer);
        }
    }


}
