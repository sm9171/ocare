package com.health.app.application.port.in;

import java.util.Optional;

import com.health.app.domain.health.DailyStatistics;

public interface GetDailyStatisticsUseCase {

    Optional<DailyStatistics> getDailyStatistics(GetDailyStatisticsQuery query);
}
