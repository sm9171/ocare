package com.health.app.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.health.app.domain.health.MonthlyStatistics;

public record MonthlyStatisticsResponse(
        Long id,
        String recordKey,
        Integer year,
        Integer month,
        Integer totalSteps,
        BigDecimal totalCalories,
        BigDecimal totalDistance,
        Integer activeDays,
        Integer averageStepsPerDay,
        BigDecimal averageCaloriesPerDay,
        BigDecimal averageDistancePerDay,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static MonthlyStatisticsResponse from(MonthlyStatistics monthlyStatistics) {
        return new MonthlyStatisticsResponse(
                monthlyStatistics.getId(),
                monthlyStatistics.getRecordKey().getValue(),
                monthlyStatistics.getYear(),
                monthlyStatistics.getMonth(),
                monthlyStatistics.getTotalSteps().getValue(),
                monthlyStatistics.getTotalCalories().getValue(),
                monthlyStatistics.getTotalDistance().getValue(),
                monthlyStatistics.getActiveDays(),
                monthlyStatistics.getAverageStepsPerDay().getValue(),
                monthlyStatistics.getAverageCaloriesPerDay().getValue(),
                monthlyStatistics.getAverageDistancePerDay().getValue(),
                monthlyStatistics.getCreatedAt(),
                monthlyStatistics.getUpdatedAt());
    }
}
