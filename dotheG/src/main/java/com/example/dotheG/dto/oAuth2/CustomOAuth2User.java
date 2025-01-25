package com.example.dotheG.dto.oAuth2;

import com.example.dotheG.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDto memberDto;

    public CustomOAuth2User(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    //받은 데이터 값 리턴, 제공자별로 획일화하기 어려워서 일단 null return
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    //role 획득 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return memberDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberDto.getUserName();
    }

    public String getUserLogin() {
        return memberDto.getUserLogin();
    }
}
