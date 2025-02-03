package com.example.dotheG.controller;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.notification.MemberAlertListResponseDto;
import com.example.dotheG.dto.notification.MemberAlertRequestDto;
import com.example.dotheG.exception.CustomException;
import com.example.dotheG.exception.ErrorCode;
import com.example.dotheG.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/notifications")
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    // 알람 전체 목록 조회
    @GetMapping
    public Response<List<MemberAlertListResponseDto>> getNotifications() {

        return Response.success("알람목록 조회", notificationService.getNotifications());
    }

    // 알람 상세 목록 조회(읽음처리)
    @GetMapping("/{userAlertId}")
    public Response<?> getNotificationInfo(@PathVariable Long userAlertId) {
        notificationService.getNotification(userAlertId);
        return Response.success("알람 상세 조회",null);
    }

    @PostMapping
    public Response<?> saveNotification(@RequestBody MemberAlertRequestDto requestDto) {
        notificationService.saveMemberAlert(requestDto);
        return Response.success("알람 저장 성공", null);
    }

    // 2주지난 알람은 자동삭제 TODO 스케쥴러 사용
    // 매월 1일 10시 월간 성과보고서 알람 -> 전체 TODO 스케쥴러 사용
    // 매주 월요일 10시 주간 성과보고서 알람 -> 전체 TODO 스케쥴러 사용
    // 목표 걸음(7,000보) 달성 시 데일리 미션 알람 -> 미션달성자
    // 목표 걸음(50,000보) 달성 시 주간 미션 알람 -> 미션 달성자
    // 오늘 퀘스트를 하나도 등록하지 않은 경우 매일 18시 알람 -> 퀘스트 미수행자 TODO 스케쥴러 사용
    // 오늘 퀴즈를 풀지 않은 경우 매일 20시 알람 -> 퀴즈 미수행자 TODO 스케쥴러 사용


}
