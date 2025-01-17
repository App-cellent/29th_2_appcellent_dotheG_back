package com.example.dotheG.service;

import com.example.dotheG.dto.*;
import com.example.dotheG.model.Member;
import com.example.dotheG.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        System.out.println(oAuth2User);

        //서비스가 어디서 온 요청인지 구분, 소셜 로그인 플랫폼 따라..
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else { return null; }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듦
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        Member existData = memberRepository.findByUserLogin(username);

        //우리 서버에 데이터 존재 x 경우
        if (existData == null) {

            Member member = Member.builder()
                    .userLogin(username)
                    .email(oAuth2Response.getEmail())
                    .userName(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);

            MemberDto memberDto = MemberDto.builder()
                    .userLogin(username)
                    .userName(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(memberDto);

        } else {
            existData.updateUserInfo(oAuth2Response.getName(), oAuth2Response.getEmail());

            memberRepository.save(existData);

            MemberDto memberDto = MemberDto.builder()
                    .userLogin(existData.getUserLogin())
                    .userName(oAuth2Response.getName())
                    .role(existData.getRole())
                    .build();

            return new CustomOAuth2User(memberDto);
        }
    }
}
