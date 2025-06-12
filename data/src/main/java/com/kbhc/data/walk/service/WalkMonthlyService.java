package com.kbhc.data.walk.service;

import com.kbhc.data.walk.entity.WalkMonthlyEntity;
import com.kbhc.data.walk.repository.WalkMonthlyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalkMonthlyService {
    private final WalkMonthlyRepository walkMonthlyRepository;

    public List<WalkMonthlyEntity> findAllWalkMonthlyBy(UUID recordKey) {
        return walkMonthlyRepository.findAllWalkMonthlyBy(recordKey);
    }

    public List<WalkMonthlyEntity> findAllWalkMonthlyBy(UUID recordKey, List<YearMonth> targetMonths) {
        return walkMonthlyRepository.findAllWalkMonthlyBy(recordKey, targetMonths);
    }

    public List<WalkMonthlyEntity> saveAll(List<WalkMonthlyEntity> walkMonthlyEntities) {
        return walkMonthlyRepository.saveAll(walkMonthlyEntities);
    }
}
