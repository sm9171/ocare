package com.health.app.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyStatisticsJpaRepository extends JpaRepository<MonthlyStatisticsJpaEntity, Long> {

    Optional<MonthlyStatisticsJpaEntity> findByRecordKeyAndYearAndMonth(
            String recordKey, int year, int month);

    List<MonthlyStatisticsJpaEntity> findByRecordKeyAndYearOrderByMonth(
            String recordKey, int year);
}