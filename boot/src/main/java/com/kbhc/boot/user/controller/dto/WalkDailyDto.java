package com.kbhc.boot.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class WalkDailyDto {
    private UUID recordKey;
    private List<WalkDailySummary> dailySummaries;

    @Builder
    public WalkDailyDto(UUID recordKey, List<WalkDailySummary> dailySummaries) {
        this.recordKey = recordKey;
        this.dailySummaries = dailySummaries;
    }

    @Getter
    @NoArgsConstructor
    public static class WalkDailySummary {
        private LocalDate date;
        private BigDecimal steps;
        private BigDecimal calories;
        private BigDecimal distance;

        @Builder
        public WalkDailySummary(LocalDate date, BigDecimal steps, BigDecimal calories, BigDecimal distance) {
            this.date = date;
            this.steps = steps;
            this.calories = calories;
            this.distance = distance;
        }
    }
}
