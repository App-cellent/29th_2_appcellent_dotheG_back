package com.example.dotheG.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.checkerframework.checker.units.qual.N;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {
    private String userName;
    private String userLogin;
}
