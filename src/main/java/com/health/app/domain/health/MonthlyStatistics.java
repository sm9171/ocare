package com.health.app.domain.health;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import com.health.app.domain.common.RecordKey;

public class MonthlyStatistics {

    private RecordKey recordKey;
    private int year;
    private int month;
    private Steps totalSteps;
    private Calories totalCalories;
    private Distance totalDistance;
    private int activeDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long id;

    protected MonthlyStatistics() {}

    public MonthlyStatistics(RecordKey recordKey, int year, int month) {
        this.recordKey = validateRecordKey(recordKey);
        this.year = validateYear(year);
        this.month = validateMonth(month);
        this.totalSteps = new Steps(0);
        this.totalCalories = new Calories(0);
        this.totalDistance = new Distance(0);
        this.activeDays = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public MonthlyStatistics(
            Long id,
            RecordKey recordKey,
            int year,
            int month,
            Steps totalSteps,
            Calories totalCalories,
            Distance totalDistance,
            int activeDays,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.recordKey = recordKey;
        this.year = year;
        this.month = month;
        this.totalSteps = totalSteps;
        this.totalCalories = totalCalories;
        this.totalDistance = totalDistance;
        this.activeDays = activeDays;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MonthlyStatistics aggregate(
            RecordKey recordKey, int year, int month, List<DailyStatistics> dailyStatisticsList) {
        MonthlyStatistics statistics = new MonthlyStatistics(recordKey, year, month);

        for (DailyStatistics dailyStats : dailyStatisticsList) {
            statistics.addDailyStatistics(dailyStats);
        }

        return statistics;
    }

    public void addDailyStatistics(DailyStatistics dailyStatistics) {
        validateDailyStatisticsForAggregation(dailyStatistics);

        this.totalSteps = this.totalSteps.add(dailyStatistics.getTotalSteps());
        this.totalCalories = this.totalCalories.add(dailyStatistics.getTotalCalories());
        this.totalDistance = this.totalDistance.add(dailyStatistics.getTotalDistance());

        if (hasActivity(dailyStatistics)) {
            this.activeDays++;
        }

        this.updatedAt = LocalDateTime.now();
    }


    public Steps getAverageStepsPerDay() {
        if (activeDays == 0) return new Steps(0);
        return new Steps(totalSteps.getValue() / activeDays);
    }

    public Calories getAverageCaloriesPerDay() {
        if (activeDays == 0) return new Calories(0);
        return new Calories(totalCalories.getDoubleValue() / activeDays);
    }

    public Distance getAverageDistancePerDay() {
        if (activeDays == 0) return new Distance(0);
        return new Distance(totalDistance.getDoubleValue() / activeDays);
    }

    private boolean hasActivity(DailyStatistics dailyStatistics) {
        return dailyStatistics.getTotalSteps().getValue() > 0
                || dailyStatistics.getTotalCalories().getDoubleValue() > 0
                || dailyStatistics.getTotalDistance().getDoubleValue() > 0;
    }

    private void validateDailyStatisticsForAggregation(DailyStatistics dailyStatistics) {
        if (!this.recordKey.equals(dailyStatistics.getRecordKey())) {
            throw new IllegalArgumentException("DailyStatistics RecordKey does not match");
        }
        YearMonth thisMonth = YearMonth.of(this.year, this.month);
        YearMonth dailyMonth = YearMonth.from(dailyStatistics.getStatisticsDate());
        if (!thisMonth.equals(dailyMonth)) {
            throw new IllegalArgumentException("DailyStatistics date does not match month");
        }
    }

    private RecordKey validateRecordKey(RecordKey recordKey) {
        if (recordKey == null) {
            throw new IllegalArgumentException("RecordKey cannot be null");
        }
        return recordKey;
    }

    private int validateYear(int year) {
        if (year < 2020 || year > 2100) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        return year;
    }

    private int validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        return month;
    }

    public RecordKey getRecordKey() {
        return recordKey;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Steps getTotalSteps() {
        return totalSteps;
    }

    public Calories getTotalCalories() {
        return totalCalories;
    }

    public Distance getTotalDistance() {
        return totalDistance;
    }

    public int getActiveDays() {
        return activeDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MonthlyStatistics that = (MonthlyStatistics) obj;
        return Objects.equals(recordKey, that.recordKey)
                && year == that.year
                && month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, year, month);
    }
}
