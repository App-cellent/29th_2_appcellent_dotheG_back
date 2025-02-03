package com.example.dotheG.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAlertListResponseDto {
    // 알람 아이디
    private Long id;
    // 알람 내용
    private String message;
    // 읽음 여부
    private boolean isRead;
    // 받은 시각
    private LocalDateTime timestamp;

}
