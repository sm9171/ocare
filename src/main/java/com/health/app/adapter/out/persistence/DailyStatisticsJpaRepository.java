package com.health.app.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyStatisticsJpaRepository extends JpaRepository<DailyStatisticsJpaEntity, Long> {

    Optional<DailyStatisticsJpaEntity> findByRecordKeyAndStatisticsDate(
            String recordKey, LocalDate date);

    @Query(
            "SELECT d FROM DailyStatisticsJpaEntity d WHERE d.recordKey = :recordKey "
                    + "AND EXTRACT(YEAR FROM d.statisticsDate) = :year AND EXTRACT(MONTH FROM d.statisticsDate) = :month "
                    + "ORDER BY d.statisticsDate")
    List<DailyStatisticsJpaEntity> findByRecordKeyAndYearAndMonth(
            @Param("recordKey") String recordKey,
            @Param("year") int year,
            @Param("month") int month);

    @Query(
            "SELECT d FROM DailyStatisticsJpaEntity d WHERE d.recordKey = :recordKey AND"
                + " d.statisticsDate BETWEEN :startDate AND :endDate ORDER BY d.statisticsDate")
    List<DailyStatisticsJpaEntity> findByRecordKeyAndStatisticsDateBetween(
            @Param("recordKey") String recordKey,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}