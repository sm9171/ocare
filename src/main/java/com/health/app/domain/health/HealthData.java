package com.health.app.domain.health;

import java.time.LocalDateTime;
import java.util.Objects;

import com.health.app.domain.common.RecordKey;

public class HealthData {

    private RecordKey recordKey;
    private Steps steps;
    private Calories calories;
    private Distance distance;
    private LocalDateTime collectedAt;
    private LocalDateTime createdAt;

    private Long id;

    protected HealthData() {}

    public HealthData(RecordKey recordKey, Steps steps, Calories calories, Distance distance) {
        this.recordKey = validateRecordKey(recordKey);
        this.steps = validateSteps(steps);
        this.calories = validateCalories(calories);
        this.distance = validateDistance(distance);
        this.collectedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    public HealthData(
            RecordKey recordKey,
            Steps steps,
            Calories calories,
            Distance distance,
            LocalDateTime collectedAt) {
        this.recordKey = validateRecordKey(recordKey);
        this.steps = validateSteps(steps);
        this.calories = validateCalories(calories);
        this.distance = validateDistance(distance);
        this.collectedAt = validateCollectedAt(collectedAt);
        this.createdAt = LocalDateTime.now();
    }

    public HealthData(
            Long id,
            RecordKey recordKey,
            Steps steps,
            Calories calories,
            Distance distance,
            LocalDateTime collectedAt,
            LocalDateTime createdAt) {
        this.id = id;
        this.recordKey = recordKey;
        this.steps = steps;
        this.calories = calories;
        this.distance = distance;
        this.collectedAt = collectedAt;
        this.createdAt = createdAt;
    }

    public boolean isFromSameDay(HealthData other) {
        return this.collectedAt.toLocalDate().equals(other.collectedAt.toLocalDate());
    }

    public boolean isFromSameUser(HealthData other) {
        return this.recordKey.equals(other.recordKey);
    }

    private RecordKey validateRecordKey(RecordKey recordKey) {
        if (recordKey == null) {
            throw new IllegalArgumentException("RecordKey cannot be null");
        }
        return recordKey;
    }

    private Steps validateSteps(Steps steps) {
        if (steps == null) {
            throw new IllegalArgumentException("Steps cannot be null");
        }
        return steps;
    }

    private Calories validateCalories(Calories calories) {
        if (calories == null) {
            throw new IllegalArgumentException("Calories cannot be null");
        }
        return calories;
    }

    private Distance validateDistance(Distance distance) {
        if (distance == null) {
            throw new IllegalArgumentException("Distance cannot be null");
        }
        return distance;
    }

    private LocalDateTime validateCollectedAt(LocalDateTime collectedAt) {
        if (collectedAt == null) {
            throw new IllegalArgumentException("CollectedAt cannot be null");
        }
        if (collectedAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("CollectedAt cannot be in the future");
        }
        return collectedAt;
    }

    public RecordKey getRecordKey() {
        return recordKey;
    }

    public Steps getSteps() {
        return steps;
    }

    public Calories getCalories() {
        return calories;
    }

    public Distance getDistance() {
        return distance;
    }

    public LocalDateTime getCollectedAt() {
        return collectedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HealthData that = (HealthData) obj;
        return Objects.equals(recordKey, that.recordKey)
                && Objects.equals(collectedAt, that.collectedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey, collectedAt);
    }
}
