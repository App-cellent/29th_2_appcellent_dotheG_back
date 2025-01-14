package com.example.dotheG.controller;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.step.StepRewardStateResponseDto;
import com.example.dotheG.dto.step.StepSummaryResponseDto;
import com.example.dotheG.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/steps")
@RequestMapping("/steps")
@RequiredArgsConstructor
public class StepController {
    private final StepService stepService;

    @GetMapping("/test")
    public Response<?> testAPI() {
        return Response.success("테스트입니다.", null);

    }
    // 실시간 걸음수 업데이트
    @PatchMapping("/update")
    public Response<Object> updateStep(@RequestParam int steps) {
        return Response.success("걸음수 업데이트", stepService.updateStep(steps));
    }

    // 걸음수 요약 보고서(일일, 주간, 월간, 누적, 탄소배출량)
    @GetMapping("/summary")
    public Response<StepSummaryResponseDto> getStepSummary() {
        return Response.success("걸음수 반환", stepService.getStepSummary());
    }

    @PatchMapping("/reward/{period}")
    public Response<Object> getRewardFromStep(@PathVariable String period) {

        return stepService.getReward(period);
    }

    @GetMapping("/reward/state")
    public Response<StepRewardStateResponseDto> getRewardStateFromStep() {
        return Response.success("리워드 지급 상태반환", stepService.getStepRewardState());
    }

}
