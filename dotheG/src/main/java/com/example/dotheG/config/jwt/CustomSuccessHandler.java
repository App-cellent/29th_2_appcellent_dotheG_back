package com.example.dotheG.config.jwt;
import com.example.dotheG.dto.oAuth2.CustomOAuth2User;
import com.example.dotheG.model.Refresh;
import com.example.dotheG.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

//    @Value("${redirect.uri}")
//    private String redirectUri;

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public CustomSuccessHandler(JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //oAuth2User username, role 값 뽑아오기
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String username = customOAuth2User.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority grantedAuthority = iterator.next();
        String role = grantedAuthority.getAuthority();

        String access = jwtUtil.createToken("access", username, role, 600000*36L);
        String refresh = jwtUtil.createToken("refresh", username, role, 86400000L);

        addRefreshToken(username, refresh, 86400000L);

        log.info("로그인 인증 성공한 user: {}", username);

         //JSON 응답을 만들어 클라이언트로 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\":\"" + access + "\"}");


        //response.setHeader("access", access);
        //쿠키 방식으로 토큰 생성
        //response.addCookie(createCookie("refresh", refresh));
        //response.setStatus(HttpStatus.OK.value());
//        response.sendRedirect("dotheg://oauth/callback");

        log.info("토큰 생성 완 및 리디렉션 완료");
    }

    private void addRefreshToken(String username, String refreshToken, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refreshEntity = Refresh.builder()
                .userLogin(username)
                .refresh(refreshToken)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refreshEntity);
    }
    //쿠키 생성 메소드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(600000*36);
        //https 에서만 사용하도록 하는 내용
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}