package com.health.app.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.MonthlyStatistics;
import com.health.app.domain.health.Steps;

import jakarta.persistence.*;

@Entity
@Table(
        name = "monthly_health_statistics",
        indexes = {
            @Index(
                    name = "idx_monthly_statistics_record_key_year_month",
                    columnList = "record_key, year, month_value",
                    unique = true)
        })
public class MonthlyStatisticsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_key", nullable = false, length = 100)
    private String recordKey;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "month_value", nullable = false)
    private Integer month;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps;

    @Column(name = "total_calories", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCalories;

    @Column(name = "total_distance", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalDistance;

    @Column(name = "active_days", nullable = false)
    private Integer activeDays;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected MonthlyStatisticsJpaEntity() {}

    public MonthlyStatisticsJpaEntity(
            String recordKey,
            Integer year,
            Integer month,
            Integer totalSteps,
            BigDecimal totalCalories,
            BigDecimal totalDistance,
            Integer activeDays,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
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

    public static MonthlyStatisticsJpaEntity fromDomain(MonthlyStatistics monthlyStatistics) {
        return new MonthlyStatisticsJpaEntity(
                monthlyStatistics.getRecordKey().getValue(),
                monthlyStatistics.getYear(),
                monthlyStatistics.getMonth(),
                monthlyStatistics.getTotalSteps().getValue(),
                monthlyStatistics.getTotalCalories().getValue(),
                monthlyStatistics.getTotalDistance().getValue(),
                monthlyStatistics.getActiveDays(),
                monthlyStatistics.getCreatedAt(),
                monthlyStatistics.getUpdatedAt());
    }

    public MonthlyStatistics toDomain() {
        return new MonthlyStatistics(
                this.id,
                new RecordKey(this.recordKey),
                this.year,
                this.month,
                new Steps(this.totalSteps),
                new Calories(this.totalCalories),
                new Distance(this.totalDistance),
                this.activeDays,
                this.createdAt,
                this.updatedAt);
    }

    public Long getId() {
        return id;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getMonth() {
        return month;
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

    public Integer getActiveDays() {
        return activeDays;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
