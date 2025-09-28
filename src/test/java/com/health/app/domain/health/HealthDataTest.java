package com.health.app.domain.health;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.health.app.domain.common.RecordKey;

class HealthDataTest {

    @Test
    void createHealthData_ValidData_Success() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        // when
        HealthData healthData = new HealthData(recordKey, steps, calories, distance);

        // then
        assertThat(healthData.getRecordKey()).isEqualTo(recordKey);
        assertThat(healthData.getSteps()).isEqualTo(steps);
        assertThat(healthData.getCalories()).isEqualTo(calories);
        assertThat(healthData.getDistance()).isEqualTo(distance);
        assertThat(healthData.getCollectedAt()).isNotNull();
        assertThat(healthData.getCreatedAt()).isNotNull();
    }

    @Test
    void createHealthData_WithCollectedAt_Success() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);
        LocalDateTime collectedAt = LocalDateTime.now().minusHours(1);

        // when
        HealthData healthData = new HealthData(recordKey, steps, calories, distance, collectedAt);

        // then
        assertThat(healthData.getCollectedAt()).isEqualTo(collectedAt);
    }

    @Test
    void createHealthData_NullRecordKey_ThrowsException() {
        // given
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        // when & then
        assertThatThrownBy(() -> new HealthData(null, steps, calories, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null");
    }

    @Test
    void createHealthData_FutureCollectedAt_ThrowsException() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);
        LocalDateTime futureTime = LocalDateTime.now().plusHours(1);

        // when & then
        assertThatThrownBy(() -> new HealthData(recordKey, steps, calories, distance, futureTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CollectedAt cannot be in the future");
    }

    @Test
    void isFromSameDay_SameDate_ReturnsTrue() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        LocalDateTime time1 = LocalDateTime.of(2023, 12, 25, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 12, 25, 14, 30);

        HealthData data1 = new HealthData(recordKey, steps, calories, distance, time1);
        HealthData data2 = new HealthData(recordKey, steps, calories, distance, time2);

        // when & then
        assertThat(data1.isFromSameDay(data2)).isTrue();
    }

    @Test
    void isFromSameDay_DifferentDate_ReturnsFalse() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        LocalDateTime time1 = LocalDateTime.of(2023, 12, 25, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 12, 26, 10, 0);

        HealthData data1 = new HealthData(recordKey, steps, calories, distance, time1);
        HealthData data2 = new HealthData(recordKey, steps, calories, distance, time2);

        // when & then
        assertThat(data1.isFromSameDay(data2)).isFalse();
    }

    @Test
    void isFromSameUser_SameRecordKey_ReturnsTrue() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        HealthData data1 = new HealthData(recordKey, steps, calories, distance);
        HealthData data2 =
                new HealthData(recordKey, new Steps(3000), new Calories(150.0), new Distance(2.0));

        // when & then
        assertThat(data1.isFromSameUser(data2)).isTrue();
    }

    @Test
    void isFromSameUser_DifferentRecordKey_ReturnsFalse() {
        // given
        RecordKey recordKey1 = new RecordKey("USER_123");
        RecordKey recordKey2 = new RecordKey("USER_456");
        Steps steps = new Steps(5000);
        Calories calories = new Calories(250.5);
        Distance distance = new Distance(3.2);

        HealthData data1 = new HealthData(recordKey1, steps, calories, distance);
        HealthData data2 = new HealthData(recordKey2, steps, calories, distance);

        // when & then
        assertThat(data1.isFromSameUser(data2)).isFalse();
    }
}
