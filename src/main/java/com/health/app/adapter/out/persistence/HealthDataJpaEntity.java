package com.health.app.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.HealthData;
import com.health.app.domain.health.Steps;

import jakarta.persistence.*;

@Entity
@Table(
        name = "health_data",
        indexes = {
            @Index(
                    name = "idx_health_data_record_key_collected_at",
                    columnList = "record_key, collected_at")
        })
public class HealthDataJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_key", nullable = false, length = 100)
    private String recordKey;

    @Column(name = "steps", nullable = false)
    private Integer steps;

    @Column(name = "calories", nullable = false, precision = 10, scale = 2)
    private BigDecimal calories;

    @Column(name = "distance", nullable = false, precision = 10, scale = 3)
    private BigDecimal distance;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected HealthDataJpaEntity() {}

    public HealthDataJpaEntity(
            String recordKey,
            Integer steps,
            BigDecimal calories,
            BigDecimal distance,
            LocalDateTime collectedAt,
            LocalDateTime createdAt) {
        this.recordKey = recordKey;
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.collectedAt = collectedAt;
        this.createdAt = createdAt;
    }

    public static HealthDataJpaEntity fromDomain(HealthData healthData) {
        return new HealthDataJpaEntity(
                healthData.getRecordKey().getValue(),
                healthData.getSteps().getValue(),
                healthData.getCalories().getValue(),
                healthData.getDistance().getValue(),
                healthData.getCollectedAt(),
                healthData.getCreatedAt());
    }

    public HealthData toDomain() {
        return new HealthData(
                this.id,
                new RecordKey(this.recordKey),
                new Steps(this.steps),
                new Calories(this.calories),
                new Distance(this.distance),
                this.collectedAt,
                this.createdAt);
    }

    public Long getId() {
        return id;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public Integer getSteps() {
        return steps;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
