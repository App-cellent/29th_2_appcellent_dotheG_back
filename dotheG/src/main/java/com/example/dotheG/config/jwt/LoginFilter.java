package com.example.dotheG.config.jwt;

import com.example.dotheG.dto.LoginDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.Refresh;
import com.example.dotheG.model.Withdraw;
import com.example.dotheG.repository.MemberRepository;
import com.example.dotheG.repository.RefreshRepository;
import com.example.dotheG.repository.WithdrawRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private MemberRepository memberRepository;
    private RefreshRepository refreshRepository;
    private WithdrawRepository withdrawRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MemberRepository memberRepository, RefreshRepository refreshRepository, WithdrawRepository withdrawRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.refreshRepository = refreshRepository;
        this.withdrawRepository = withdrawRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginDto loginDto = new LoginDto();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDto = objectMapper.readValue(messageBody, LoginDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(loginDto.getUsername());

        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        log.info("[LoginFilter] user {}의 authentication 얻기 위한 시도", username);

        Member member = memberRepository.findByUserLogin(username);
        Optional<Withdraw> withdraw = withdrawRepository.findByUserId(member);
        if (withdraw.isPresent()) {
            log.warn("[LoginFilter] 탈퇴한 User {}. Login 거부.", username);
            throw new AuthenticationException("탈퇴한 계정입니다.") {};
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        String userLogin = authentication.getName();
        log.info("[LoginFilter] Login successful for user: {}", userLogin);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        log.info("[LoginFilter] Role: {}", role);


        String access = jwtUtil.createToken("access", userLogin, role, 600000*36L);
        String refresh = jwtUtil.createToken("refresh", userLogin, role, 86400000L);

        addRefresh(userLogin, refresh, 86400000L);

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        log.info("[LoginFilter] 토큰 정상적으로 발행된 user: {}", userLogin);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.warn("[LoginFilter] Authentication 실패: {}", failed.getMessage());
        log.warn("[LoginFilter] Authentication 실패한 IP 주소: {}", request.getRemoteAddr());
        String userLogin = obtainUsername(request);
        log.warn("[LoginFilter] 로그인 실패 user: {} - Reason: {}", userLogin, failed.getMessage());
        response.setStatus(401);
    }

    private void addRefresh(String userLogin, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = Refresh.builder()
                .userLogin(userLogin)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
        log.info("리프레쉬 토큰 성공적 발급한 user: {}", userLogin);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //Https 경로면 아래 내용 추가
        //cookie.setSecure(true)
        //cookie.setPath("/");
        cookie.setHttpOnly(true);
        log.info("다음 키를 위한 쿠키 발급: {}", key);
        return cookie;
    }
}
