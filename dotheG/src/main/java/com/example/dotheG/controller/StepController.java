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

    // FIXME 객체생성은 12시 지나면 모든유저 동일하니깐 api없어도 됨
    // FIXME userId를 @PathVariable로 불러오기로 전부 수정해두고, 나중에 authentication사용으로 변경

    @GetMapping("/test")
    public Response<?> testAPI() {
        return Response.success("테스트입니다.", null);

    }
    // 실시간 걸음수 업데이트
    // TODO 유저 개인화 작업 필요
    @PatchMapping("/update/{userId}")
    public Response<Object> updateStep(@PathVariable Long userId, @RequestParam int steps) {
        return Response.success("걸음수 업데이트", stepService.updateStep(userId, steps));
    }

    // 걸음수 요약 보고서(일일, 주간, 월간, 누적, 탄소배출량)
    // TODO 유저 개인화 작업 필요
    @GetMapping("/summary/{userId}")
    public Response<StepSummaryResponseDto> getStepSummary(@PathVariable Long userId) {
        return Response.success("걸음수 반환", stepService.getStepSummary(userId));
    }

    // TODO 프론트에서 if문으로 7000걸음넘었을떄만 해당api호출할수있는지 물어보기 / 안되면 매번 검증하고, 달성시 지급
    // TODO 유저 개인화 작업 필요
    @PatchMapping("/reward/{userId}")
    public Response<Object> getRewardFromStep(@PathVariable Long userId) {

        return stepService.getReward(userId);
    }
}
