package com.example.dotheG.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDto memberDto;

    public CustomOAuth2User(MemberDto memberDto) {
        this.memberDto = memberDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDto.getRole();
            }
        });

        return authorities;
    }

    @Override
    public String getName() {
        return memberDto.getUserName();
    }

    public String getUsername() {
        return memberDto.getUserLogin();
    }
}
