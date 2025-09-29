package com.health.app.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthDataJpaRepository extends JpaRepository<HealthDataJpaEntity, Long> {

    List<HealthDataJpaEntity> findByRecordKey(String recordKey);

    @Query(
            "SELECT h FROM HealthDataJpaEntity h WHERE h.recordKey = :recordKey "
                    + "AND h.collectedAt BETWEEN :startDate AND :endDate ORDER BY h.collectedAt")
    List<HealthDataJpaEntity> findByRecordKeyAndCollectedAtBetween(
            @Param("recordKey") String recordKey,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(
            "SELECT h FROM HealthDataJpaEntity h WHERE h.recordKey = :recordKey "
                    + "AND CAST(h.collectedAt AS DATE) = CAST(:date AS DATE) ORDER BY h.collectedAt")
    List<HealthDataJpaEntity> findByRecordKeyAndDate(
            @Param("recordKey") String recordKey, @Param("date") LocalDateTime date);
}
