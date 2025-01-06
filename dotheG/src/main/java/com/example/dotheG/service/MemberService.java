package com.example.dotheG.service;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.model.Member;
import com.example.dotheG.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
    }

}