//package com.example.dotheG.config;
//
//import jakarta.annotation.PostConstruct;
//import org.opencv.core.Core;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OpenCVInitializer {
//
//    @PostConstruct
//    public void OpenCVInitializer(){
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // OpenCV 네이티브 라이브러리 로드
//        System.out.println("OpenCV Library Loaded: " + Core.NATIVE_LIBRARY_NAME);
//    }
//}