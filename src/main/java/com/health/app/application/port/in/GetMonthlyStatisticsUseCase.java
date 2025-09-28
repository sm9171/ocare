package com.health.app.application.port.in;

import java.util.Optional;

import com.health.app.domain.health.MonthlyStatistics;

public interface GetMonthlyStatisticsUseCase {

    Optional<MonthlyStatistics> getMonthlyStatistics(GetMonthlyStatisticsQuery query);
}
