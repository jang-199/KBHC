package com.kbhc.data.walk.repository;

import com.kbhc.data.walk.entity.WalkMonthlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface WalkMonthlyRepository extends JpaRepository<WalkMonthlyEntity, UUID> {

    @Query("SELECT w " +
            "FROM WalkMonthlyEntity w " +
            "WHERE w.recordKey = :recordKey " +
            "ORDER BY w.month desc")
    List<WalkMonthlyEntity> findAllWalkMonthlyBy(@Param("recordKey") UUID recordKey);

    @Query("SELECT w " +
            "FROM WalkMonthlyEntity w " +
            "WHERE w.recordKey = :recordKey AND w.month in :targetMonths " +
            "ORDER BY w.month desc")
    List<WalkMonthlyEntity> findAllWalkMonthlyBy(@Param("recordKey") UUID recordKey,
                                                 @Param("targetMonths") List<YearMonth> targetMonths);
}
