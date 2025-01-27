package com.example.dotheG.config.jwt;

import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/logout$")) {
            log.warn("로그아웃 요청이 아님 URI: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            log.warn("잘못된 요청 메소드: {}", requestMethod);
            chain.doFilter(request, response);
            return;
        }

        log.info("로그아웃 요청 확인됨. 쿠키 확인중...");
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                log.info("리프레시 토큰 발견");
            }
        }

        if (refresh == null) {
            log.error("리프레시 토큰이 존재하지 않음");
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        try {
            jwtUtil.isExpired(refresh);
            log.info("리프레시 토큰 유효함");
        } catch (ExpiredJwtException e) {
            log.error("리프레시 토큰 만료됨");
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String userLogin = jwtUtil.getUsername(refresh);

        refreshRepository.deleteByUserLogin(userLogin);
        log.info("사용자의 모든 리프레시 토큰 삭제완료");

//        String category = jwtUtil.getCategory(refresh);
//        if (!category.equals("refresh")) {
//            log.error("잘못된 토큰 유형: {}", category);
//            throw new CustomException(ErrorCode.INVALID_TOKEN);
//        }
//
//        Boolean isExist = refreshRepository.existsByRefresh(refresh);
//        if (!isExist) {
//            log.error("리프레시 토큰이 DB에 존재하지 않음");
//            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
//        }
//
//        refreshRepository.deleteByRefresh(refresh);
//        log.info("리프레시 토큰 삭제 완료");

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        log.info("로그아웃 성공, 리프레시 토큰 삭제됨");
    }
}