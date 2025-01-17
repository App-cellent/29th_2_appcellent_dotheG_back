package com.example.dotheG.controller;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.WeeklyReportResponseDto;
import com.example.dotheG.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 주간 보고서 저장
    @PostMapping("/weekly/save")
    public Response<String> saveWeeklyReport() {
        reportService.saveWeeklyReport();
        return Response.success("주간 보고서 저장 성공", null);
    }

    // 주간 보고서 조회
    @GetMapping("/weekly")
    public Response<WeeklyReportResponseDto> getWeeklyReport() {
        WeeklyReportResponseDto weeklyReport = reportService.getWeeklyReport();
        return Response.success("주간 보고서 조회 성공", weeklyReport);
    }
}
