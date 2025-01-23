package com.example.dotheG.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String userName;
    private String userLogin;
    private boolean isNoti;
}
