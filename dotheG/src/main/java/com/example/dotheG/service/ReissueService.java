package com.example.dotheG.service;

import com.example.dotheG.config.jwt.JwtUtil;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.model.Refresh;
import com.example.dotheG.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }

        String userLogin = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createToken("access", userLogin, role, 3600000L);
        String newRefresh = jwtUtil.createToken("refresh", userLogin, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefresh(userLogin, newRefresh, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void addRefresh(String userLogin, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .userLogin(userLogin)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true)
        //cookie.setPath("/")
        cookie.setHttpOnly(true);

        return cookie;
    }


}