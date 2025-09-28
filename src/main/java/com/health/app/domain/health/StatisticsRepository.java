package com.health.app.domain.health;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.health.app.domain.common.RecordKey;

public interface StatisticsRepository {

    DailyStatistics saveDailyStatistics(DailyStatistics dailyStatistics);

    MonthlyStatistics saveMonthlyStatistics(MonthlyStatistics monthlyStatistics);

    Optional<DailyStatistics> findDailyStatistics(RecordKey recordKey, LocalDate date);

    Optional<MonthlyStatistics> findMonthlyStatistics(RecordKey recordKey, int year, int month);

    List<DailyStatistics> findDailyStatisticsByMonth(RecordKey recordKey, int year, int month);

    List<DailyStatistics> findDailyStatisticsByDateRange(
            RecordKey recordKey, LocalDate startDate, LocalDate endDate);

    List<MonthlyStatistics> findMonthlyStatisticsByYear(RecordKey recordKey, int year);

    void deleteDailyStatistics(DailyStatistics dailyStatistics);

    void deleteMonthlyStatistics(MonthlyStatistics monthlyStatistics);
}
