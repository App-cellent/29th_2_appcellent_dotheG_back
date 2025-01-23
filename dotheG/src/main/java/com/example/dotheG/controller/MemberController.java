package com.example.dotheG.controller;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/users")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public Response<String> SignUp(MemberDto memberDto) {

        memberService.SignUp(memberDto);

        return Response.success("회원가입이 성공적으로 처리되었습니다.", memberDto.getUserName());
    }
}