package com.example.dotheG.dto.mypage;

import lombok.Getter;

@Getter
public class PasswordDto {
    private String currentPassword;
    private String newPassword;
    private String confirmedPassword;
}
