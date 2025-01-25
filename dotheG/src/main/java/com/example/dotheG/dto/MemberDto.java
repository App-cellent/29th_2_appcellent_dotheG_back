package com.example.dotheG.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDto {

    private String userName;

    private String userLogin;

    private String userPassword;

    private boolean available;

    private boolean isSocial;

    private String role = "USER";

    @Builder
    public MemberDto(String userLogin, String userName, String userPassword, boolean available, boolean isSocial, String role) {
        this.userName = userName;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.available = available;
        this.isSocial = isSocial;
        this.role = role;
    }
}