package com.example.dotheG.controller;

import com.example.dotheG.config.jwt.JwtUtil;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/users")
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public Response<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        reissueService.reissue(request, response);
        return Response.success("토큰이 재발급되었습니다.", "success");
    }

}