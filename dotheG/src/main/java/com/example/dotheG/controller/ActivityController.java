package com.example.dotheG.controller;

import com.example.dotheG.dto.ActivityResponseDto;
import com.example.dotheG.dto.MemberActivityDto;
import com.example.dotheG.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/upload")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // OpenCV 사용 (사진 분석)
    @GetMapping("/analyze")
    public ResponseEntity<Long> analyzePhoto(@RequestPart("activityImage") MultipartFile activityImage){
        try {
            Long analyzedActivityId = activityService.analyzePhoto(activityImage);
            return ResponseEntity.ok(analyzedActivityId);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 퀘스트 등록하기 (사진 결과 저장)
    // [이미지, 활동 명, 날짜] 저장되어야 함 + 리워드 지급 받아야 함
    @PostMapping("/certification")
    public ResponseEntity<String> requestImage(
            @RequestPart(value = "file", required = false) MultipartFile activityImage,
            @RequestParam("userInfoId") Long userInfoId) {
        try {
            activityService.requestImage(activityImage, userInfoId);
            return ResponseEntity.ok("Certification successfully registered.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // 오늘의 인증 조회하기
    @GetMapping("/today")
    public ResponseEntity<List<ActivityResponseDto>> viewToday(@RequestBody MemberActivityDto memberActivityDto) {
        List<ActivityResponseDto> activities = activityService.viewToday(memberActivityDto.getUserId());

        if (activities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(activities);
    }

}
