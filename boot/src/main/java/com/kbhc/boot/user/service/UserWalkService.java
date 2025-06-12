package com.kbhc.boot.user.service;

import com.kbhc.boot.user.controller.dto.WalkDailyDto;
import com.kbhc.boot.user.controller.dto.WalkDto;
import com.kbhc.boot.user.controller.dto.WalkMonthlyDto;
import com.kbhc.boot.user.mapper.WalkMapper;
import com.kbhc.data.walk.entity.WalkDailyEntity;
import com.kbhc.data.walk.entity.WalkMonthlyEntity;
import com.kbhc.data.walk.service.WalkDailyService;
import com.kbhc.data.walk.service.WalkMonthlyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWalkService {
    private final WalkDailyService walkDailyService;
    private final WalkMonthlyService walkMonthlyService;
    private final WalkMapper walkMapper;

    /***
     * recordKey를 기준으로 월별 집계 데이터를 조회하여 반환합니다.
     * Cf.조회된 데이터는 Redis에 10Sec 케싱됩니다.
     */
    @Cacheable(value = "walkMonthlySummary", key = "#recordKey")
    @Transactional(readOnly = true)
    public WalkMonthlyDto findAllWalkMonthlySummaryBy(UUID recordKey) {
        return walkMapper.toWalkMonthlyDto(walkMonthlyService.findAllWalkMonthlyBy(recordKey));
    }

    /***
     * recordKey를 기준으로 일별 집계 데이터를 조회하여 반환합니다.
     * Cf.조회된 데이터는 Redis에 10Sec 케싱됩니다.
     */
    @Cacheable(value = "walkDailySummary", key = "#recordKey")
    @Transactional(readOnly = true)
    public WalkDailyDto findAllWalkDailySummaryBy(UUID recordKey) {
        return walkMapper.toWalkDailyDto(walkDailyService.findAllWalkDailyBy(recordKey));
    }

    /***
     * 1.DTO를 바탕으로 일별, 월별 정보를 키로 가지는 Map을 각각 생성합니다.
     * 2.각 키의 날짜를 IN절로 가지는 WalkDailyEntity와 WalkMonthlyEntity를 recordKey를 기준으로 조회합니다.
     * 3.맨 처음 생성한 맵과 비교하여 겹치는 날짜에는 데이터를 더하고, 없는 날짜는 엔티티를 추가로 생성하여 저장합니다.
     */
    @Transactional
    public void saveWalkData(WalkDto.Request request) {
        UUID recordKey = request.getRecordkey();
        Map<LocalDate, WalkSummary> localDateDailySummaryMap = summarizeByDate(request);
        Map<YearMonth, WalkSummary> yearMonthDailySummaryMap = summarizeByMonth(request);

        List<WalkDailyEntity> walkDailies = walkDailyService.findAllWalkDailyBy(recordKey, localDateDailySummaryMap.keySet().stream().toList());
        List<WalkMonthlyEntity> allWalkMonthlyBy = walkMonthlyService.findAllWalkMonthlyBy(recordKey, yearMonthDailySummaryMap.keySet().stream().toList());

        List<WalkDailyEntity> walkDailyEntities = upsertDailyWalks(walkDailies, localDateDailySummaryMap, recordKey);
        List<WalkMonthlyEntity> walkMonthlyEntities = upsertMonthlyWalks(allWalkMonthlyBy, yearMonthDailySummaryMap, recordKey);

        walkDailyService.saveAll(walkDailyEntities);
        walkMonthlyService.saveAll(walkMonthlyEntities);
    }

    private static List<WalkMonthlyEntity> upsertMonthlyWalks(List<WalkMonthlyEntity> walkMonthlies, Map<YearMonth, WalkSummary> monthlySummaryMap, UUID recordKey) {
        Map<YearMonth, WalkMonthlyEntity> existingMonthlyMap = walkMonthlies.stream()
                .collect(Collectors.toMap(WalkMonthlyEntity::getMonth, Function.identity()));

        List<WalkMonthlyEntity> entitiesToSave = new ArrayList<>();

        for (Map.Entry<YearMonth, WalkSummary> entry : monthlySummaryMap.entrySet()) {
            YearMonth yearMonth = entry.getKey();
            WalkSummary summary = entry.getValue();

            WalkMonthlyEntity entity = existingMonthlyMap.get(yearMonth);

            if (Objects.isNull(entity)) {
                entity = WalkMonthlyEntity.builder()
                        .recordKey(recordKey)
                        .month(yearMonth)
                        .steps(summary.getTotalSteps())
                        .calories(summary.getTotalCalories())
                        .distance(summary.getTotalDistance())
                        .build();
            } else {
                entity.addSteps(summary.getTotalSteps());
                entity.addCalories(summary.getTotalCalories());
                entity.addDistance(summary.getTotalDistance());
            }

            entitiesToSave.add(entity);
        }

        return entitiesToSave;
    }

    private static List<WalkDailyEntity> upsertDailyWalks(List<WalkDailyEntity> walkDailyEntities, Map<LocalDate, WalkSummary> dateSummaryMap, UUID recordKey) {
        Map<LocalDate, WalkDailyEntity> existingDailyMap = walkDailyEntities.stream()
                .collect(Collectors.toMap(WalkDailyEntity::getDate, Function.identity()));

        List<WalkDailyEntity> entitiesToSave = new ArrayList<>();

        for (Map.Entry<LocalDate, WalkSummary> entry : dateSummaryMap.entrySet()) {
            LocalDate date = entry.getKey();
            WalkSummary summary = entry.getValue();

            WalkDailyEntity entity = existingDailyMap.get(date);

            if (Objects.isNull(entity)) {
                entity = WalkDailyEntity.builder()
                        .recordKey(recordKey)
                        .date(date)
                        .steps(summary.getTotalSteps())
                        .calories(summary.getTotalCalories())
                        .distance(summary.getTotalDistance())
                        .build();

            } else {
                entity.addSteps(summary.getTotalSteps());
                entity.addCalories(summary.getTotalCalories());
                entity.addDistance(summary.getTotalDistance());
            }

            entitiesToSave.add(entity);
        }

        return entitiesToSave;
    }

    private static Map<YearMonth, WalkSummary> summarizeByMonth(WalkDto.Request request) {
        return summarizeBy(request, YearMonth::from);
    }

    private static Map<LocalDate, WalkSummary> summarizeByDate(WalkDto.Request request) {
        return summarizeBy(request, OffsetDateTime::toLocalDate);
    }

    private static <K> Map<K, WalkSummary> summarizeBy(
            WalkDto.Request request,
            Function<OffsetDateTime, K> keyExtractor
    ) {
        Map<K, WalkSummary> summaryMap = new HashMap<>();

        for (WalkDto.Entry entry : request.getData().getEntries()) {
            if (entry.getPeriod() == null || entry.getPeriod().getFrom() == null) {
                continue;
            }

            K key = keyExtractor.apply(entry.getPeriod().getFrom());
            summaryMap.putIfAbsent(key, new WalkSummary());

            WalkSummary summary = summaryMap.get(key);
            summary.addSteps(entry.getSteps());
            if (entry.getCalories() != null) {
                summary.addCalories(entry.getCalories().getValue());
            }
            if (entry.getDistance() != null) {
                summary.addDistance(entry.getDistance().getValue());
            }
        }

        return summaryMap;
    }

    @Getter
    private static class WalkSummary {
        private BigDecimal totalSteps = BigDecimal.ZERO;
        private BigDecimal totalCalories = BigDecimal.ZERO;
        private BigDecimal totalDistance = BigDecimal.ZERO;

        public void addSteps(BigDecimal steps) {
            if (!Objects.isNull(steps)){
                this.totalSteps = this.totalSteps.add(steps);
            }
        }

        public void addCalories(BigDecimal calories) {
            if (!Objects.isNull(calories)){
                this.totalCalories = this.totalCalories.add(calories);
            }
        }

        public void addDistance(BigDecimal distance) {
            if (!Objects.isNull(distance)){
                this.totalDistance = this.totalDistance.add(distance);
            }
        }
    }
}
