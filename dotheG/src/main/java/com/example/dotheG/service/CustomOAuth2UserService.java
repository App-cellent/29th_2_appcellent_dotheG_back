package com.example.dotheG.service;
import com.example.dotheG.dto.MemberDto;
import com.example.dotheG.dto.oAuth2.CustomOAuth2User;
import com.example.dotheG.dto.oAuth2.NaverResponse;
import com.example.dotheG.dto.oAuth2.OAuth2Response;
import com.example.dotheG.model.Member;
import com.example.dotheG.model.MemberInfo;
import com.example.dotheG.model.MemberQuiz;
import com.example.dotheG.model.Step;
import com.example.dotheG.repository.MemberInfoRepository;
import com.example.dotheG.repository.MemberQuizRepository;
import com.example.dotheG.repository.MemberRepository;
import com.example.dotheG.repository.StepRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final StepRepository stepRepository;
    private final MemberQuizRepository memberQuizRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository, MemberInfoRepository memberInfoRepository, StepRepository stepRepository, MemberQuizRepository memberQuizRepository) {
        this.memberRepository = memberRepository;
        this.memberInfoRepository = memberInfoRepository;
        this.stepRepository = stepRepository;
        this.memberQuizRepository = memberQuizRepository;
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
                    .Noti(true)
                    .tutorial(false)
                    .isSocial(true)
                    .available(true)
                    .build();
            memberRepository.save(member);
            log.info("회원 정보 DB 저장 완료 userLogin: {}", userLogin);

            MemberInfo memberInfo = new MemberInfo(member);
            memberInfoRepository.save(memberInfo);

            Step step = new Step(member);
            stepRepository.save(step);

            MemberQuiz memberQuiz = new MemberQuiz(member);
            memberQuizRepository.save(memberQuiz);

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