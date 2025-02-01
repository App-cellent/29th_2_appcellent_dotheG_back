package com.example.dotheG.service;

import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.dto.oAuth2.CustomOAuth2User;
import com.example.dotheG.dto.oAuth2.NaverResponse;
import com.example.dotheG.dto.oAuth2.OAuth2Response;
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
        //클래스가 가지는
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println(oAuth2User);

        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        //초기화
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
//        else if (registrationId.equals("google")) {
//
//        }
        else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다");
        }

        String userLogin = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        //유저 아이디로 회원 DB에 존재하는지 조회
        Member existData = memberRepository.findByUserLogin(userLogin);

        if (existData == null) {
            Member member = Member.builder()
                    .userName(oAuth2Response.getName())
                    .userLogin(userLogin)
                    .role("ROLE_USER")
                    .email(oAuth2Response.getEmail())
                    .build();

            memberRepository.save(member);

            MemberDto memberDto = MemberDto.builder()
                    .userLogin(userLogin)
                    .userName(oAuth2Response.getName())
                    .role("ROLE_USER")
                    .build();

            return new CustomOAuth2User(memberDto);
        }
        else {
            //Role 추가
            existData.updateSocialMember(oAuth2Response.getName(),oAuth2Response.getEmail());

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
