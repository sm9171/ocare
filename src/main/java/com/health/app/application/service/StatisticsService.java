package com.health.app.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.health.app.application.port.in.GetDailyStatisticsQuery;
import com.health.app.application.port.in.GetDailyStatisticsUseCase;
import com.health.app.application.port.in.GetMonthlyStatisticsQuery;
import com.health.app.application.port.in.GetMonthlyStatisticsUseCase;
import com.health.app.application.port.out.LoadStatisticsPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.MonthlyStatistics;

@Service
@Transactional(readOnly = true)
public class StatisticsService implements GetDailyStatisticsUseCase, GetMonthlyStatisticsUseCase {

    private final LoadStatisticsPort loadStatisticsPort;

    public StatisticsService(LoadStatisticsPort loadStatisticsPort) {
        this.loadStatisticsPort = loadStatisticsPort;
    }

    @Override
    public Optional<DailyStatistics> getDailyStatistics(GetDailyStatisticsQuery query) {
        RecordKey recordKey = new RecordKey(query.recordKey());
        return loadStatisticsPort.loadDailyStatistics(recordKey, query.date());
    }

    @Override
    public Optional<MonthlyStatistics> getMonthlyStatistics(GetMonthlyStatisticsQuery query) {
        RecordKey recordKey = new RecordKey(query.recordKey());
        return loadStatisticsPort.loadMonthlyStatistics(recordKey, query.year(), query.month());
    }
}
