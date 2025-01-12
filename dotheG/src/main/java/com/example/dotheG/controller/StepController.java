package com.example.dotheG.controller;

import com.example.dotheG.dto.Response;
import com.example.dotheG.dto.step.StepSummaryResponseDto;
import com.example.dotheG.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/steps")
@RequestMapping("/steps")
@RequiredArgsConstructor
public class StepController {
    private final StepService stepService;

    @GetMapping("/test/api")
    public String test() {
        return "두더지 서버 테스트";
    }

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

    // TODO 프론트에서 if문으로 7000걸음넘었을떄만 해당api호출할수있는지 물어보기 / 안되면 매번 검증하고, 달성시 지급
    @PatchMapping("/reward")
    public Response<Object> getRewardFromStep() {

        return stepService.getReward();
    }

}
