package com.example.dotheG.dto;

import lombok.Getter;

@Getter
public class MemberDto {

    private final String userName;

    private final String userLogin;

    private String userPassword;

    private final boolean available;

    private final boolean isSocial;

    private final String role;

    public MemberDto(String userLogin, String userName, String userPassword) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = true;
        this.isSocial = false;
        this.role = "USER";
    }
}