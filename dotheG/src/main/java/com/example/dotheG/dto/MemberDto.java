package com.example.dotheG.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

    private final String userName;

    private final String userLogin;

    private String userPassword;

    private final boolean available;

    private final boolean isSocial;

    private final String role;

    private final String email;

    @Builder
    public MemberDto(String userLogin, String userName, String userPassword, boolean available, boolean isSocial, String role, String email) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = true;
        this.isSocial = isSocial;
        this.role = "USER";
        this.email = email;
    }
}