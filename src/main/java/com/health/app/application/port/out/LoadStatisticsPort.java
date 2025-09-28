package com.health.app.application.port.out;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.MonthlyStatistics;

public interface LoadStatisticsPort {

    Optional<DailyStatistics> loadDailyStatistics(RecordKey recordKey, LocalDate date);

    Optional<MonthlyStatistics> loadMonthlyStatistics(RecordKey recordKey, int year, int month);

    List<DailyStatistics> loadDailyStatisticsByMonth(RecordKey recordKey, int year, int month);

    List<DailyStatistics> loadDailyStatisticsByDateRange(
            RecordKey recordKey, LocalDate startDate, LocalDate endDate);

    List<MonthlyStatistics> loadMonthlyStatisticsByYear(RecordKey recordKey, int year);
}
