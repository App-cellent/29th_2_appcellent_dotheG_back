package com.example.dotheG.dto.notification;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Getter
@NoArgsConstructor
public class MemberAlertRequestDto {
    private String title;
    private String message;
}
