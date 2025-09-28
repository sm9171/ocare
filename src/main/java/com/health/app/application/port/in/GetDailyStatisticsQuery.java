package com.health.app.application.port.in;

import java.time.LocalDate;

public record GetDailyStatisticsQuery(String recordKey, LocalDate date) {
    public GetDailyStatisticsQuery {
        if (recordKey == null || recordKey.trim().isEmpty()) {
            throw new IllegalArgumentException("RecordKey cannot be null or empty");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }
}
