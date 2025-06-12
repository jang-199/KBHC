package com.kbhc.data.walk.entity;

import com.kbhc.data.common.entity.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class WalkDailyEntity extends BaseEntity {
    @Id @Tsid
    private Long id;

    @Column(nullable = false)
    private UUID recordKey;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 25, scale = 19)
    private BigDecimal steps;

    @Column(nullable = false, precision = 25, scale = 19)
    private BigDecimal distance;

    @Column(nullable = false, precision = 25, scale = 19)
    private BigDecimal calories;

    @Builder
    public WalkDailyEntity(UUID recordKey, LocalDate date, BigDecimal steps, BigDecimal distance, BigDecimal calories) {
        this.recordKey = recordKey;
        this.date = date;
        this.steps = steps;
        this.distance = distance;
        this.calories = calories;
    }

    public void addSteps(BigDecimal steps) {
        if (Objects.isNull(this.steps)) {
            this.steps = BigDecimal.ZERO;
        }
        this.steps = this.steps.add(steps);
    }

    public void addDistance(BigDecimal distance) {
        if (Objects.isNull(this.distance)) {
            this.distance = BigDecimal.ZERO;
        }
        this.distance = this.distance.add(distance);
    }

    public void addCalories(BigDecimal calories) {
        if (Objects.isNull(this.calories)) {
            this.calories = BigDecimal.ZERO;
        }
        this.calories = this.calories.add(calories);
    }
}
