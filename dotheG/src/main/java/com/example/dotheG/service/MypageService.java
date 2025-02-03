package com.example.dotheG.service;

import com.example.dotheG.dto.mypage.MyPageResponseDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.Withdraw;
import com.example.dotheG.repository.MemberRepository;
import com.example.dotheG.repository.WithdrawRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MemberRepository memberRepository;
    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void changeName(String newName) {
        Member member = memberService.getCurrentMember();

        // Fixme 중복확인 안해도 됨?
        if(newName == null || newName.isEmpty()){
            throw new CustomException(ErrorCode.NAME_NOT_WRITTEN);
        } if (newName.length() < 2 || newName.length() > 10) {
            throw new CustomException(ErrorCode.NAME_FAILED);
        }

        member.changeName(newName);
        memberRepository.save(member);
    }

    public void checkCurrentPassword(String currentPassword){
        Member member = memberService.getCurrentMember();
        System.out.println(member.getUserPassword());

        if(!bCryptPasswordEncoder.matches(currentPassword, member.getUserPassword())){
            throw new CustomException(ErrorCode.CURRENT_PASSWORD_DIFFERENT);
        }
    }

    public void changePassword(String currentPassword, String newPassword, String confirmedPassword) {
        checkCurrentPassword(currentPassword);

        Member member = memberService.getCurrentMember();

        // Todo : 비밀번호 조건 확인

        if (!newPassword.equals(confirmedPassword)){
            throw new CustomException(ErrorCode.PASSWORD_DIFFERENT);
        } else {
            member.changePassword(bCryptPasswordEncoder.encode(newPassword));
            memberRepository.save(member);
        }
    }

    public void withdraw(String currentPassword, String withdrawalReason) {
        checkCurrentPassword(currentPassword);

        Member member = memberService.getCurrentMember();

        if (withdrawRepository.findByUserId(member).isPresent()){
            throw new CustomException(ErrorCode.WITHDRAW_ALREADY);
        } else {
            // withdraw 테이블에 객체 생성
            Withdraw withdraw = new Withdraw(member, withdrawalReason);
            withdrawRepository.save(withdraw);
        }
    }

    @Transactional // Member 객체 상태가 변경될 때 JPA가 자동 감지하도록
    public void toggleNoti() {
        Member member = memberService.getCurrentMember();
        member.toggleNoti(); // on <-> off
    }

    public MyPageResponseDto getUserInfo() {
        Member member = memberService.getCurrentMember();
        return new MyPageResponseDto(
                member.getUserName(),
                member.getUserLogin(),
                member.isNoti()
        );
    }
}
