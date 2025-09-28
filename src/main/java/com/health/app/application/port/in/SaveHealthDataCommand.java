package com.health.app.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaveHealthDataCommand(
        String recordKey,
        int steps,
        BigDecimal calories,
        BigDecimal distance,
        LocalDateTime collectedAt) {
    public SaveHealthDataCommand {
        if (recordKey == null || recordKey.trim().isEmpty()) {
            throw new IllegalArgumentException("RecordKey cannot be null or empty");
        }
        if (steps < 0) {
            throw new IllegalArgumentException("Steps cannot be negative");
        }
        if (calories == null || calories.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Calories cannot be null or negative");
        }
        if (distance == null || distance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Distance cannot be null or negative");
        }
    }
}
