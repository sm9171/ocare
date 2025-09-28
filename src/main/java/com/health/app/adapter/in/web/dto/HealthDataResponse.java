package com.health.app.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.health.app.domain.health.HealthData;

public record HealthDataResponse(
        Long id,
        String recordKey,
        Integer steps,
        BigDecimal calories,
        BigDecimal distance,
        LocalDateTime collectedAt,
        LocalDateTime createdAt) {
    public static HealthDataResponse from(HealthData healthData) {
        return new HealthDataResponse(
                healthData.getId(),
                healthData.getRecordKey().getValue(),
                healthData.getSteps().getValue(),
                healthData.getCalories().getValue(),
                healthData.getDistance().getValue(),
                healthData.getCollectedAt(),
                healthData.getCreatedAt());
    }
}
