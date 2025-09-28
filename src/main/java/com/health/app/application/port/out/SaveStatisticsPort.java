package com.health.app.application.port.out;

import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.MonthlyStatistics;

public interface SaveStatisticsPort {

    DailyStatistics saveDailyStatistics(DailyStatistics dailyStatistics);

    MonthlyStatistics saveMonthlyStatistics(MonthlyStatistics monthlyStatistics);
}
