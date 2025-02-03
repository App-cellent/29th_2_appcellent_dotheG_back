package com.example.dotheG.controller;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/check-userlogin")
    public Response<String> CheckUserLogin(@RequestParam("userLogin") String userLogin) {
        boolean isAvailable = memberService.checkUserLogin(userLogin);

        if (isAvailable) {
            return Response.success("사용 가능한 아이디입니다.", userLogin);
        } else {
            return Response.fail("중복되는 아이디입니다.");
        }
    }

    @PostMapping("/check-username")
    public Response<String> CheckUserName(@RequestParam("userName") String userName) {
        boolean isAvailable = memberService.checkUserName(userName);

        if (isAvailable) {
            return Response.success("사용 가능한 닉네임입니다.", userName);
        } else {
            return Response.fail("중복 닉네임입니다.");
        }
    }

}