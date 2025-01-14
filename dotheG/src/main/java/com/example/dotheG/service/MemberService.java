package com.example.dotheG.service;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.Step;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.repository.MemberRepository;
import com.example.dotheG.repository.StepRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StepRepository stepRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberQuizRepository memberQuizRepository;


    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, StepRepository stepRepository, MemberInfoRepository memberInfoRepository, MemberQuizRepository memberQuizRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.stepRepository = stepRepository;
        this.memberInfoRepository = memberInfoRepository;
        this.memberQuizRepository = memberQuizRepository;
    }

    public void SignUp(MemberDto memberDto) {

        String userName = memberDto.getUserName();
        String userLogin = memberDto.getUserLogin();
        String userPassword = memberDto.getUserPassword();

        Boolean isExist = memberRepository.existsByUserLogin(userLogin);

        if (isExist) {
            throw new CustomException(ErrorCode.ID_DUPLICATED);
        }

        Member member = new Member(userName, userLogin, bCryptPasswordEncoder.encode(userPassword), true, false, "ROLE_USER", true);

        memberRepository.save(member);

        MemberInfo memberInfo = new MemberInfo(member);
        memberInfoRepository.save(memberInfo);

        Step step = new Step(member);
        stepRepository.save(step);

        MemberQuiz memberQuiz = new MemberQuiz(member);
        memberQuizRepository.save(memberQuiz);

    }

    public Member getCurrentMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member member = memberRepository.findByUserLogin(loginId);
        if (member == null){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return member;
    }
}