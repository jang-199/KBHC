package com.kbhc.data.walk.service;

import com.kbhc.data.walk.entity.WalkDailyEntity;
import com.kbhc.data.walk.repository.WalkDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalkDailyService {
    private final WalkDailyRepository walkDailyRepository;

    public List<WalkDailyEntity> findAllWalkDailyBy(UUID recordKey) {
        return walkDailyRepository.findAllWalkDailyBy(recordKey);
    }

    public List<WalkDailyEntity> findAllWalkDailyBy(UUID recordKey, List<LocalDate> targetDates) {
        return walkDailyRepository.findAllWalkDailyBy(recordKey, targetDates);
    }

    public List<WalkDailyEntity> saveAll(List<WalkDailyEntity> walkDailyEntities) {
        return walkDailyRepository.saveAll(walkDailyEntities);
    }
}
