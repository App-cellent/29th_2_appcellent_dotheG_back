package com.example.dotheG.service;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StepService stepService;
    private final MemberInfoRepository memberInfoRepository;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, StepService stepService, MemberInfoRepository memberInfoRepository) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.stepService = stepService;
        this.memberInfoRepository = memberInfoRepository;
    }

    public void SignUp(MemberDto memberDto) {

        String userName = memberDto.getUserName();
        String userLogin = memberDto.getUserLogin();
        String userPassword = memberDto.getUserPassword();

        Boolean isExist = memberRepository.existsByUserLogin(userLogin);

        if (isExist) {
            return;
        }

        Member member = new Member(userName, userLogin, bCryptPasswordEncoder.encode(userPassword), true, false, "ROLE_USER");

        memberRepository.save(member);

        MemberInfo memberInfo = new MemberInfo(member);
        memberInfoRepository.save(memberInfo);
        stepService.createStep(member);

    }



}