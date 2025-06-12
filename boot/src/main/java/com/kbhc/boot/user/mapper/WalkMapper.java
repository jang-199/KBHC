package com.kbhc.boot.user.mapper;

import com.kbhc.boot.user.controller.dto.WalkDailyDto;
import com.kbhc.boot.user.controller.dto.WalkMonthlyDto;
import com.kbhc.data.walk.entity.WalkDailyEntity;
import com.kbhc.data.walk.entity.WalkMonthlyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalkMapper {

    /***
     * WalkMonthlyEntity를 WalkMonthlyDto로 변환하는 메서드입니다.
     */
    public WalkMonthlyDto toWalkMonthlyDto(List<WalkMonthlyEntity> walkDailyEntities) {
        if (walkDailyEntities.isEmpty()) {
            return WalkMonthlyDto.builder().build();
        }
        return WalkMonthlyDto.builder()
                .recordKey(walkDailyEntities.stream().findFirst().orElseThrow().getRecordKey())
                .dailySummaries(toWalkMonthlySummary(walkDailyEntities))
                .build();
    }

    private List<WalkMonthlyDto.WalkMonthlySummary> toWalkMonthlySummary(List<WalkMonthlyEntity> walkMonthlyEntities) {
        return walkMonthlyEntities.stream()
                .map(walkDailyEntity -> WalkMonthlyDto.WalkMonthlySummary.builder()
                        .yearMonth(walkDailyEntity.getMonth())
                        .steps(walkDailyEntity.getSteps())
                        .calories(walkDailyEntity.getCalories())
                        .distance(walkDailyEntity.getDistance())
                        .build())
                .toList();
    }

    /***
     * WalkDailyEntity를 WalkDailyDto로 변환하는 메서드입니다.
     */
    public WalkDailyDto toWalkDailyDto(List<WalkDailyEntity> walkDailyEntities) {
        if (walkDailyEntities.isEmpty()) {
            return WalkDailyDto.builder().build();
        }
        return WalkDailyDto.builder()
                .recordKey(walkDailyEntities.stream().findFirst().orElseThrow().getRecordKey())
                .dailySummaries(toWalkDailySummary(walkDailyEntities))
                .build();
    }

    private List<WalkDailyDto.WalkDailySummary> toWalkDailySummary(List<WalkDailyEntity> walkDailyEntities) {
        return walkDailyEntities.stream()
                .map(walkDailyEntity -> WalkDailyDto.WalkDailySummary.builder()
                        .steps(walkDailyEntity.getSteps())
                        .date(walkDailyEntity.getDate())
                        .distance(walkDailyEntity.getDistance())
                        .calories(walkDailyEntity.getCalories())
                        .build())
                .toList();
    }
}
