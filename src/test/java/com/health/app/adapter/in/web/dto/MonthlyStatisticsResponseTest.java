package com.health.app.adapter.in.web.dto;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.MonthlyStatistics;
import com.health.app.domain.health.Steps;

@DisplayName("MonthlyStatisticsResponse DTO 테스트")
class MonthlyStatisticsResponseTest {

    @Test
    @DisplayName("도메인 객체로부터 Response 생성 성공")
    void shouldCreateResponseFromDomainObject() {
        // given
        Long id = 1L;
        RecordKey recordKey = new RecordKey("USER_123");
        int year = 2024;
        int month = 3;
        Steps totalSteps = new Steps(300000);
        Calories totalCalories = new Calories(BigDecimal.valueOf(15000.00));
        Distance totalDistance = new Distance(BigDecimal.valueOf(200.500));
        int activeDays = 25;
        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 31, 23, 59, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 4, 1, 0, 0, 0);

        MonthlyStatistics monthlyStatistics = new MonthlyStatistics(
                id,
                recordKey,
                year,
                month,
                totalSteps,
                totalCalories,
                totalDistance,
                activeDays,
                createdAt,
                updatedAt);

        // when
        MonthlyStatisticsResponse response = MonthlyStatisticsResponse.from(monthlyStatistics);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.recordKey()).isEqualTo("USER_123");
        assertThat(response.year()).isEqualTo(2024);
        assertThat(response.month()).isEqualTo(3);
        assertThat(response.totalSteps()).isEqualTo(300000);
        assertThat(response.totalCalories()).isEqualByComparingTo(BigDecimal.valueOf(15000.00));
        assertThat(response.totalDistance()).isEqualByComparingTo(BigDecimal.valueOf(200.500));
        assertThat(response.activeDays()).isEqualTo(25);
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);

        // 평균값 검증
        assertThat(response.averageStepsPerDay()).isEqualTo(12000); // 300000/25
        assertThat(response.averageCaloriesPerDay()).isEqualByComparingTo(BigDecimal.valueOf(600.00)); // 15000/25
        assertThat(response.averageDistancePerDay()).isEqualByComparingTo(BigDecimal.valueOf(8.020)); // 200.500/25
    }

    @Test
    @DisplayName("활동일이 0인 경우 평균값 처리")
    void shouldHandleZeroActiveDays() {
        // given
        MonthlyStatistics monthlyStatistics = new MonthlyStatistics(
                2L,
                new RecordKey("USER_456"),
                2024,
                2,
                new Steps(0),
                new Calories(BigDecimal.ZERO),
                new Distance(BigDecimal.ZERO),
                0,
                LocalDateTime.of(2024, 2, 29, 0, 0),
                LocalDateTime.of(2024, 2, 29, 0, 0));

        // when
        MonthlyStatisticsResponse response = MonthlyStatisticsResponse.from(monthlyStatistics);

        // then
        assertThat(response.activeDays()).isZero();
        assertThat(response.averageStepsPerDay()).isZero();
        assertThat(response.averageCaloriesPerDay()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.averageDistancePerDay()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("1월 데이터로 Response 생성")
    void shouldCreateResponseForJanuary() {
        // given
        MonthlyStatistics monthlyStatistics = new MonthlyStatistics(
                3L,
                new RecordKey("USER_789"),
                2024,
                1,
                new Steps(465000), // 31일 * 15000걸음
                new Calories(BigDecimal.valueOf(23250.00)), // 31일 * 750칼로리
                new Distance(BigDecimal.valueOf(310.000)), // 31일 * 10km
                31,
                LocalDateTime.of(2024, 1, 31, 23, 59),
                LocalDateTime.of(2024, 2, 1, 0, 0));

        // when
        MonthlyStatisticsResponse response = MonthlyStatisticsResponse.from(monthlyStatistics);

        // then
        assertThat(response.month()).isEqualTo(1);
        assertThat(response.activeDays()).isEqualTo(31);
        assertThat(response.averageStepsPerDay()).isEqualTo(15000); // 465000/31
        assertThat(response.averageCaloriesPerDay()).isEqualByComparingTo(BigDecimal.valueOf(750.00)); // 23250/31
        assertThat(response.averageDistancePerDay()).isEqualByComparingTo(BigDecimal.valueOf(10.000)); // 310/31
    }

    @Test
    @DisplayName("Record 접근자 메서드들이 올바르게 작동")
    void shouldAccessRecordFieldsCorrectly() {
        // given
        MonthlyStatisticsResponse response = new MonthlyStatisticsResponse(
                100L,
                "TEST_USER",
                2024,
                12,
                450000,
                BigDecimal.valueOf(22500.00),
                BigDecimal.valueOf(300.000),
                30,
                15000,
                BigDecimal.valueOf(750.00),
                BigDecimal.valueOf(10.000),
                LocalDateTime.of(2024, 12, 31, 23, 59),
                LocalDateTime.of(2025, 1, 1, 0, 0));

        // when & then
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.recordKey()).isEqualTo("TEST_USER");
        assertThat(response.year()).isEqualTo(2024);
        assertThat(response.month()).isEqualTo(12);
        assertThat(response.totalSteps()).isEqualTo(450000);
        assertThat(response.totalCalories()).isEqualByComparingTo(BigDecimal.valueOf(22500.00));
        assertThat(response.totalDistance()).isEqualByComparingTo(BigDecimal.valueOf(300.000));
        assertThat(response.activeDays()).isEqualTo(30);
        assertThat(response.averageStepsPerDay()).isEqualTo(15000);
        assertThat(response.averageCaloriesPerDay()).isEqualByComparingTo(BigDecimal.valueOf(750.00));
        assertThat(response.averageDistancePerDay()).isEqualByComparingTo(BigDecimal.valueOf(10.000));
    }

    @Test
    @DisplayName("Record의 equals 메서드가 올바르게 작동")
    void shouldImplementEqualsCorrectly() {
        // given
        LocalDateTime time1 = LocalDateTime.of(2024, 3, 31, 23, 59);
        LocalDateTime time2 = LocalDateTime.of(2024, 4, 1, 0, 0);

        MonthlyStatisticsResponse response1 = new MonthlyStatisticsResponse(
                1L, "USER_123", 2024, 3, 300000, BigDecimal.valueOf(15000.00),
                BigDecimal.valueOf(200.500), 25, 12000, BigDecimal.valueOf(600.00),
                BigDecimal.valueOf(8.020), time1, time2);

        MonthlyStatisticsResponse response2 = new MonthlyStatisticsResponse(
                1L, "USER_123", 2024, 3, 300000, BigDecimal.valueOf(15000.00),
                BigDecimal.valueOf(200.500), 25, 12000, BigDecimal.valueOf(600.00),
                BigDecimal.valueOf(8.020), time1, time2);

        MonthlyStatisticsResponse response3 = new MonthlyStatisticsResponse(
                2L, "USER_456", 2024, 4, 150000, BigDecimal.valueOf(7500.00),
                BigDecimal.valueOf(100.250), 20, 7500, BigDecimal.valueOf(375.00),
                BigDecimal.valueOf(5.012), time1, time2);

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
        MonthlyStatisticsResponse response = new MonthlyStatisticsResponse(
                1L, "USER_123", 2024, 3, 300000, BigDecimal.valueOf(15000.00),
                BigDecimal.valueOf(200.500), 25, 12000, BigDecimal.valueOf(600.00),
                BigDecimal.valueOf(8.020),
                LocalDateTime.of(2024, 3, 31, 23, 59),
                LocalDateTime.of(2024, 4, 1, 0, 0));

        // when
        String result = response.toString();

        // then
        assertThat(result)
                .contains("MonthlyStatisticsResponse")
                .contains("USER_123")
                .contains("2024")
                .contains("300000")
                .contains("25");
    }
}