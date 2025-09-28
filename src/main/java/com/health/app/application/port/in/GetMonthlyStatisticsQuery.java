package com.health.app.application.port.in;

public record GetMonthlyStatisticsQuery(String recordKey, int year, int month) {
    public GetMonthlyStatisticsQuery {
        if (recordKey == null || recordKey.trim().isEmpty()) {
            throw new IllegalArgumentException("RecordKey cannot be null or empty");
        }
        if (year < 2020 || year > 2100) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
    }
}
