package com.health.app.adapter.out.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.health.app.application.port.out.LoadStatisticsPort;
import com.health.app.application.port.out.SaveStatisticsPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.MonthlyStatistics;

@Component
public class StatisticsPersistenceAdapter implements LoadStatisticsPort, SaveStatisticsPort {

    private final StatisticsJpaRepository.DailyStatisticsJpaRepository dailyStatisticsRepository;
    private final StatisticsJpaRepository.MonthlyStatisticsJpaRepository
            monthlyStatisticsRepository;

    public StatisticsPersistenceAdapter(
            StatisticsJpaRepository.DailyStatisticsJpaRepository dailyStatisticsRepository,
            StatisticsJpaRepository.MonthlyStatisticsJpaRepository monthlyStatisticsRepository) {
        this.dailyStatisticsRepository = dailyStatisticsRepository;
        this.monthlyStatisticsRepository = monthlyStatisticsRepository;
    }

    @Override
    public Optional<DailyStatistics> loadDailyStatistics(RecordKey recordKey, LocalDate date) {
        return dailyStatisticsRepository
                .findByRecordKeyAndStatisticsDate(recordKey.getValue(), date)
                .map(DailyStatisticsJpaEntity::toDomain);
    }

    @Override
    public Optional<MonthlyStatistics> loadMonthlyStatistics(
            RecordKey recordKey, int year, int month) {
        return monthlyStatisticsRepository
                .findByRecordKeyAndYearAndMonth(recordKey.getValue(), year, month)
                .map(MonthlyStatisticsJpaEntity::toDomain);
    }

    @Override
    public List<DailyStatistics> loadDailyStatisticsByMonth(
            RecordKey recordKey, int year, int month) {
        return dailyStatisticsRepository
                .findByRecordKeyAndYearAndMonth(recordKey.getValue(), year, month)
                .stream()
                .map(DailyStatisticsJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<DailyStatistics> loadDailyStatisticsByDateRange(
            RecordKey recordKey, LocalDate startDate, LocalDate endDate) {
        return dailyStatisticsRepository
                .findByRecordKeyAndStatisticsDateBetween(recordKey.getValue(), startDate, endDate)
                .stream()
                .map(DailyStatisticsJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<MonthlyStatistics> loadMonthlyStatisticsByYear(RecordKey recordKey, int year) {
        return monthlyStatisticsRepository
                .findByRecordKeyAndYearOrderByMonth(recordKey.getValue(), year)
                .stream()
                .map(MonthlyStatisticsJpaEntity::toDomain)
                .toList();
    }

    @Override
    public DailyStatistics saveDailyStatistics(DailyStatistics dailyStatistics) {
        DailyStatisticsJpaEntity entity = DailyStatisticsJpaEntity.fromDomain(dailyStatistics);
        DailyStatisticsJpaEntity savedEntity = dailyStatisticsRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public MonthlyStatistics saveMonthlyStatistics(MonthlyStatistics monthlyStatistics) {
        MonthlyStatisticsJpaEntity entity =
                MonthlyStatisticsJpaEntity.fromDomain(monthlyStatistics);
        MonthlyStatisticsJpaEntity savedEntity = monthlyStatisticsRepository.save(entity);
        return savedEntity.toDomain();
    }
}
