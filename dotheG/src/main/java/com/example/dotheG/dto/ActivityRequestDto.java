package com.example.dotheG.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ActivityRequestDto {
    private MultipartFile activityImage;
    private Long userInfoId;
}
