package com.health.app.domain.health;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.health.app.domain.common.RecordKey;

public class DailyStatistics {

    private RecordKey recordKey;
    private LocalDate statisticsDate;
    private Steps totalSteps;
    private Calories totalCalories;
    private Distance totalDistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long id;

    protected DailyStatistics() {}

    public DailyStatistics(RecordKey recordKey, LocalDate statisticsDate) {
        this.recordKey = validateRecordKey(recordKey);
        this.statisticsDate = validateStatisticsDate(statisticsDate);
        this.totalSteps = new Steps(0);
        this.totalCalories = new Calories(0);
        this.totalDistance = new Distance(0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public DailyStatistics(
            Long id,
            RecordKey recordKey,
            LocalDate statisticsDate,
            Steps totalSteps,
            Calories totalCalories,
            Distance totalDistance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.recordKey = recordKey;
        this.statisticsDate = statisticsDate;
        this.totalSteps = totalSteps;
        this.totalCalories = totalCalories;
        this.totalDistance = totalDistance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DailyStatistics aggregate(
            RecordKey recordKey, LocalDate date, List<HealthData> healthDataList) {
        DailyStatistics statistics = new DailyStatistics(recordKey, date);

        for (HealthData healthData : healthDataList) {
            statistics.addHealthData(healthData);
        }

        return statistics;
    }

    public void addHealthData(HealthData healthData) {
        validateHealthDataForAggregation(healthData);

        this.totalSteps = this.totalSteps.add(healthData.getSteps());
        this.totalCalories = this.totalCalories.add(healthData.getCalories());
        this.totalDistance = this.totalDistance.add(healthData.getDistance());
        this.updatedAt = LocalDateTime.now();
    }


    private void validateHealthDataForAggregation(HealthData healthData) {
        if (!this.recordKey.equals(healthData.getRecordKey())) {
            throw new IllegalArgumentException("HealthData RecordKey does not match");
        }
        if (!this.statisticsDate.equals(healthData.getCollectedAt().toLocalDate())) {
            throw new IllegalArgumentException("HealthData date does not match statistics date");
        }
    }

    private RecordKey validateRecordKey(RecordKey recordKey) {
        if (recordKey == null) {
            throw new IllegalArgumentException("RecordKey cannot be null");
        }
        return recordKey;
    }

    private LocalDate validateStatisticsDate(LocalDate statisticsDate) {
        if (statisticsDate == null) {
            throw new IllegalArgumentException("Statistics date cannot be null");
        }
        return statisticsDate;
    }

    public RecordKey getRecordKey() {
        return recordKey;
    }

    public LocalDate getStatisticsDate() {
        return statisticsDate;
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
        DailyStatistics that = (DailyStatistics) obj;
        return Objects.equals(recordKey, that.recordKey)
                && Objects.equals(statisticsDate, that.statisticsDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, statisticsDate);
    }
}
