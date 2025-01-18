package com.example.dotheG.controller;

import com.example.dotheG.dto.activity.ActivityResponseDto;
import com.example.dotheG.dto.Response;
import com.example.dotheG.service.ActivityService;
import org.springframework.http.HttpStatus;
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
    public Response<Long> analyzePhoto(@RequestPart("activityImage") MultipartFile activityImage){
        try {
            return Response.success("이미지 분석", activityService.analyzePhoto(activityImage));
        } catch (Exception e){
            return Response.fail("이미지 분석 실패 " + HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 퀘스트 등록하기 (사진 결과 저장)
    // [이미지, 활동 명, 날짜] 저장되어야 함 + 리워드 지급 받아야 함
    @PostMapping("/certification")
    public Response<Objects> requestImage(
            @RequestPart(value = "activityImage", required = false) MultipartFile activityImage) {
        activityService.requestImage(activityImage);
        return Response.success("퀘스트 등록", null);
    }

    // 오늘의 인증 조회하기
    @GetMapping("/viewToday")
    public Response<List<ActivityResponseDto>> viewToday() {
        return Response.success("오늘 인증 기록 조회", activityService.viewToday());
    }
}
