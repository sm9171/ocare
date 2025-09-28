package com.health.app.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.health.app.domain.health.DailyStatistics;

public record DailyStatisticsResponse(
        Long id,
        String recordKey,
        LocalDate statisticsDate,
        Integer totalSteps,
        BigDecimal totalCalories,
        BigDecimal totalDistance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static DailyStatisticsResponse from(DailyStatistics dailyStatistics) {
        return new DailyStatisticsResponse(
                dailyStatistics.getId(),
                dailyStatistics.getRecordKey().getValue(),
                dailyStatistics.getStatisticsDate(),
                dailyStatistics.getTotalSteps().getValue(),
                dailyStatistics.getTotalCalories().getValue(),
                dailyStatistics.getTotalDistance().getValue(),
                dailyStatistics.getCreatedAt(),
                dailyStatistics.getUpdatedAt());
    }
}
