package com.kbhc.boot.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class WalkMonthlyDto {
    private UUID recordKey;
    private List<WalkMonthlyDto.WalkMonthlySummary> dailySummaries;

    @Builder
    public WalkMonthlyDto(UUID recordKey, List<WalkMonthlySummary> dailySummaries) {
        this.recordKey = recordKey;
        this.dailySummaries = dailySummaries;
    }

    @Getter
    @NoArgsConstructor
    public static class WalkMonthlySummary {
        private YearMonth yearMonth;
        private BigDecimal steps;
        private BigDecimal calories;
        private BigDecimal distance;

        @Builder
        public WalkMonthlySummary(YearMonth yearMonth, BigDecimal steps, BigDecimal calories, BigDecimal distance) {
            this.yearMonth = yearMonth;
            this.steps = steps;
            this.calories = calories;
            this.distance = distance;
        }
    }
}
