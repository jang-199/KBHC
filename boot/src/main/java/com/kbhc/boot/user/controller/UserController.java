package com.kbhc.boot.user.controller;

import com.kbhc.boot.user.controller.dto.WalkDailyDto;
import com.kbhc.boot.user.controller.dto.WalkDto;
import com.kbhc.boot.user.controller.dto.WalkMonthlyDto;
import com.kbhc.boot.user.service.UserWalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/users/walks")
@RestController
public class UserController {
    private final UserWalkService userWalkService;

    /***
     * 사용자의 걷기 데이터를 저장하는 API입니다.
     */
    @PostMapping
    public void saveWalkData(@RequestBody WalkDto.Request request){
        userWalkService.saveWalkData(request);
    }

    /***
     * 사용자의 일별 걷기 데이터 집계 정보를 조회하는 API입니다.
     */
    @GetMapping("/daily/{recordKey}")
    public WalkDailyDto getWalkDailySummary(@PathVariable UUID recordKey) {
        return userWalkService.findAllWalkDailySummaryBy(recordKey);
    }

    /***
     * 사용자의 월별 걷기 데이터 집계 정보를 조회하는 API입니다.
     */
    @GetMapping("/monthly/{recordKey}")
    public WalkMonthlyDto getWalkMonthlySummary(@PathVariable UUID recordKey) {
        return userWalkService.findAllWalkMonthlySummaryBy(recordKey);
    }
}
