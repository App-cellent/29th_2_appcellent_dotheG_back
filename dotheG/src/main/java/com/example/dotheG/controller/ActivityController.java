package com.example.dotheG.controller;

import com.example.dotheG.dto.ActivityResponseDto;
import com.example.dotheG.dto.MemberActivityDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/upload")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // OpenCV 사용 (사진 분석)
    @GetMapping("/analyze")
    public Response<Objects> analyzePhoto(@RequestPart("activityImage") MultipartFile activityImage){
        try {
            Long analyzedActivityId = activityService.analyzePhoto(activityImage);
            return Response.success("이미지 분석", null);
        } catch (Exception e){
            return Response.success("이미지 분석 실패" + HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    // 퀘스트 등록하기 (사진 결과 저장)
    // [이미지, 활동 명, 날짜] 저장되어야 함 + 리워드 지급 받아야 함
    @PostMapping("/certification")
    public Response<Objects> requestImage(
            @RequestPart(value = "file", required = false) MultipartFile activityImage,
            @RequestParam("userInfoId") Long userInfoId) {
        try {
            activityService.requestImage(activityImage, userInfoId);
            return Response.success("퀘스트 등록", null);
        } catch (Exception e) {
            return Response.success("퀘스트 등록 실패" + e.getMessage(), null);
        }
    }

    // 오늘의 인증 조회하기
    @GetMapping("/today")
    public Response<Objects> viewToday(@RequestBody MemberActivityDto memberActivityDto) {
        List<ActivityResponseDto> activities = activityService.viewToday(memberActivityDto.getUserId());

        if (activities.isEmpty()) {
            return Response.success("오늘 인증 기록 없음", null);
        }
        return Response.success("오늘 인증 기록 조회", null);
    }

}
