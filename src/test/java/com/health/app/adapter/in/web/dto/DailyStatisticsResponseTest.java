package com.health.app.adapter.in.web.dto;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.DailyStatistics;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.Steps;

@DisplayName("DailyStatisticsResponse DTO 테스트")
class DailyStatisticsResponseTest {

    @Test
    @DisplayName("도메인 객체로부터 Response 생성 성공")
    void shouldCreateResponseFromDomainObject() {
        // given
        Long id = 1L;
        RecordKey recordKey = new RecordKey("USER_123");
        LocalDate statisticsDate = LocalDate.of(2024, 3, 15);
        Steps totalSteps = new Steps(10000);
        Calories totalCalories = new Calories(BigDecimal.valueOf(500.50));
        Distance totalDistance = new Distance(BigDecimal.valueOf(7.250));
        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 15, 10, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 3, 15, 11, 0, 0);

        DailyStatistics dailyStatistics = new DailyStatistics(
                id,
                recordKey,
                statisticsDate,
                totalSteps,
                totalCalories,
                totalDistance,
                createdAt,
                updatedAt);

        // when
        DailyStatisticsResponse response = DailyStatisticsResponse.from(dailyStatistics);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.recordKey()).isEqualTo("USER_123");
        assertThat(response.statisticsDate()).isEqualTo(statisticsDate);
        assertThat(response.totalSteps()).isEqualTo(10000);
        assertThat(response.totalCalories()).isEqualByComparingTo(BigDecimal.valueOf(500.50));
        assertThat(response.totalDistance()).isEqualByComparingTo(BigDecimal.valueOf(7.250));
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("0값들을 가진 도메인 객체로부터 Response 생성")
    void shouldCreateResponseFromDomainObjectWithZeroValues() {
        // given
        Long id = 2L;
        RecordKey recordKey = new RecordKey("USER_456");
        LocalDate statisticsDate = LocalDate.of(2024, 1, 1);
        Steps totalSteps = new Steps(0);
        Calories totalCalories = new Calories(BigDecimal.ZERO);
        Distance totalDistance = new Distance(BigDecimal.ZERO);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0);

        DailyStatistics dailyStatistics = new DailyStatistics(
                id,
                recordKey,
                statisticsDate,
                totalSteps,
                totalCalories,
                totalDistance,
                createdAt,
                updatedAt);

        // when
        DailyStatisticsResponse response = DailyStatisticsResponse.from(dailyStatistics);

        // then
        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.recordKey()).isEqualTo("USER_456");
        assertThat(response.totalSteps()).isZero();
        assertThat(response.totalCalories()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.totalDistance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Record 접근자 메서드들이 올바르게 작동")
    void shouldAccessRecordFieldsCorrectly() {
        // given
        DailyStatisticsResponse response = new DailyStatisticsResponse(
                100L,
                "TEST_USER",
                LocalDate.of(2024, 12, 25),
                15000,
                BigDecimal.valueOf(750.75),
                BigDecimal.valueOf(10.500),
                LocalDateTime.of(2024, 12, 25, 8, 30),
                LocalDateTime.of(2024, 12, 25, 18, 30));

        // when & then
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.recordKey()).isEqualTo("TEST_USER");
        assertThat(response.statisticsDate()).isEqualTo(LocalDate.of(2024, 12, 25));
        assertThat(response.totalSteps()).isEqualTo(15000);
        assertThat(response.totalCalories()).isEqualByComparingTo(BigDecimal.valueOf(750.75));
        assertThat(response.totalDistance()).isEqualByComparingTo(BigDecimal.valueOf(10.500));
        assertThat(response.createdAt()).isEqualTo(LocalDateTime.of(2024, 12, 25, 8, 30));
        assertThat(response.updatedAt()).isEqualTo(LocalDateTime.of(2024, 12, 25, 18, 30));
    }

    @Test
    @DisplayName("Record의 equals 메서드가 올바르게 작동")
    void shouldImplementEqualsCorrectly() {
        // given
        LocalDate date = LocalDate.of(2024, 3, 15);
        LocalDateTime time1 = LocalDateTime.of(2024, 3, 15, 10, 0);
        LocalDateTime time2 = LocalDateTime.of(2024, 3, 15, 11, 0);

        DailyStatisticsResponse response1 = new DailyStatisticsResponse(
                1L, "USER_123", date, 10000, BigDecimal.valueOf(500.50), 
                BigDecimal.valueOf(7.250), time1, time2);

        DailyStatisticsResponse response2 = new DailyStatisticsResponse(
                1L, "USER_123", date, 10000, BigDecimal.valueOf(500.50), 
                BigDecimal.valueOf(7.250), time1, time2);

        DailyStatisticsResponse response3 = new DailyStatisticsResponse(
                2L, "USER_456", date, 5000, BigDecimal.valueOf(250.25), 
                BigDecimal.valueOf(3.500), time1, time2);

        // when & then
        assertThat(response1)
                .isEqualTo(response2)
                .isNotEqualTo(response3)
                .hasSameHashCodeAs(response2);
    }

    @Test
    @DisplayName("Record의 toString 메서드가 올바르게 작동")
    void shouldImplementToStringCorrectly() {
        // given
        DailyStatisticsResponse response = new DailyStatisticsResponse(
                1L,
                "USER_123",
                LocalDate.of(2024, 3, 15),
                10000,
                BigDecimal.valueOf(500.50),
                BigDecimal.valueOf(7.250),
                LocalDateTime.of(2024, 3, 15, 10, 0),
                LocalDateTime.of(2024, 3, 15, 11, 0));

        // when
        String result = response.toString();

        // then
        assertThat(result)
                .contains("DailyStatisticsResponse")
                .contains("USER_123")
                .contains("10000")
                .contains("500.5");
    }
}