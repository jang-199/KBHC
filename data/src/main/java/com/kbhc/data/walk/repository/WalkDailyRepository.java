package com.kbhc.data.walk.repository;

import com.kbhc.data.walk.entity.WalkDailyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WalkDailyRepository extends JpaRepository<WalkDailyEntity, UUID> {

    @Query("SELECT w " +
            "FROM WalkDailyEntity w " +
            "WHERE w.recordKey = :recordKey " +
            "ORDER BY w.date desc ")
    List<WalkDailyEntity> findAllWalkDailyBy(@Param("recordKey") UUID recordKey);

    @Query("SELECT w " +
            "FROM WalkDailyEntity w " +
            "WHERE w.recordKey = :recordKey AND w.date in :targetDates " +
            "ORDER BY w.date desc ")
    List<WalkDailyEntity> findAllWalkDailyBy(@Param("recordKey") UUID recordKey,
                                             @Param("targetDates") List<LocalDate> targetDates);
}
