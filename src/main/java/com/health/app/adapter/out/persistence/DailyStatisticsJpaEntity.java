package com.health.app.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.Steps;

import jakarta.persistence.*;

@Entity
@Table(
        name = "daily_health_statistics",
        indexes = {
            @Index(
                    name = "idx_daily_statistics_record_key_date",
                    columnList = "record_key, statistics_date",
                    unique = true)
        })
public class DailyStatisticsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_key", nullable = false, length = 100)
    private String recordKey;

    @Column(name = "statistics_date", nullable = false)
    private LocalDate statisticsDate;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps;

    @Column(name = "total_calories", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCalories;

    @Column(name = "total_distance", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalDistance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected DailyStatisticsJpaEntity() {}

    public DailyStatisticsJpaEntity(
            String recordKey,
            LocalDate statisticsDate,
            Integer totalSteps,
            BigDecimal totalCalories,
            BigDecimal totalDistance,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.recordKey = recordKey;
        this.statisticsDate = statisticsDate;
        this.totalSteps = totalSteps;
        this.totalCalories = totalCalories;
        this.totalDistance = totalDistance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DailyStatisticsJpaEntity fromDomain(DailyStatistics dailyStatistics) {
        return new DailyStatisticsJpaEntity(
                dailyStatistics.getRecordKey().getValue(),
                dailyStatistics.getStatisticsDate(),
                dailyStatistics.getTotalSteps().getValue(),
                dailyStatistics.getTotalCalories().getValue(),
                dailyStatistics.getTotalDistance().getValue(),
                dailyStatistics.getCreatedAt(),
                dailyStatistics.getUpdatedAt());
    }

    public DailyStatistics toDomain() {
        return new DailyStatistics(
                this.id,
                new RecordKey(this.recordKey),
                this.statisticsDate,
                new Steps(this.totalSteps),
                new Calories(this.totalCalories),
                new Distance(this.totalDistance),
                this.createdAt,
                this.updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public LocalDate getStatisticsDate() {
        return statisticsDate;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public BigDecimal getTotalCalories() {
        return totalCalories;
    }

    public BigDecimal getTotalDistance() {
        return totalDistance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
