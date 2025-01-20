package com.example.dotheG.controller;

import com.example.dotheG.dto.mypage.MyPageResponseDto;
import com.example.dotheG.dto.mypage.PasswordDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.mypage.WithdrawDto;
import com.example.dotheG.model.Member;
import com.example.dotheG.service.MypageService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    // 닉네임 변경
    @PatchMapping("/changeName")
    public Response<Objects> changeName(@RequestParam String newName){
        mypageService.changeName(newName);
        return Response.success("닉네임 변경 성공", null);
    }

    // 비밀번호 변경
    @PatchMapping("/changePassword")
    public Response<Objects> changePassword(@RequestBody PasswordDto passwordDto){
        mypageService.changePassword(passwordDto.getCurrentPassword(), passwordDto.getNewPassword(), passwordDto.getConfirmedPassword());
        return Response.success("비밀번호 변경 성공", null);
    }

    // 탈퇴 처리
    @PostMapping("/withdraw")
    public Response<Objects> withdraw(@RequestBody WithdrawDto withdrawDto) {
        mypageService.withdraw(withdrawDto.getCurrentPassword(), withdrawDto.getWithdrawalReason());
        return Response.success("탈퇴 처리 성공", null);
    }

    // 알림 on/off
    @PatchMapping("/toggleNoti")
    public Response<Objects> toggleNoti(){
        mypageService.toggleNoti();
        return Response.success("알림 설정 변경 완료", null);
    }

    @GetMapping("/info")
    public Response<MyPageResponseDto> info(){
        return Response.success("마이페이지 정보 조회", mypageService.getUserInfo());
    }
}

