package com.health.app.application.port.in;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GetMonthlyStatisticsQuery 테스트")
class GetMonthlyStatisticsQueryTest {

    @Test
    @DisplayName("유효한 쿼리 생성 성공")
    void shouldCreateValidQuery() {
        // given & when
        var query = new GetMonthlyStatisticsQuery("user123", 2024, 3);

        // then
        assertThat(query.recordKey()).isEqualTo("user123");
        assertThat(query.year()).isEqualTo(2024);
        assertThat(query.month()).isEqualTo(3);
    }

    @Test
    @DisplayName("recordKey가 null인 경우 예외 발생")
    void shouldThrowExceptionForNullRecordKey() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery(null, 2024, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    @DisplayName("recordKey가 빈 문자열인 경우 예외 발생")
    void shouldThrowExceptionForEmptyRecordKey() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("", 2024, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    @DisplayName("recordKey가 공백만 포함한 경우 예외 발생")
    void shouldThrowExceptionForBlankRecordKey() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("   ", 2024, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    @DisplayName("year가 최소값보다 작은 경우 예외 발생")
    void shouldThrowExceptionForTooSmallYear() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("user123", 2019, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid year: 2019");
    }

    @Test
    @DisplayName("year가 최대값보다 큰 경우 예외 발생")
    void shouldThrowExceptionForTooLargeYear() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("user123", 2101, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid year: 2101");
    }

    @Test
    @DisplayName("month가 1보다 작은 경우 예외 발생")
    void shouldThrowExceptionForTooSmallMonth() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("user123", 2024, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid month: 0");
    }

    @Test
    @DisplayName("month가 12보다 큰 경우 예외 발생")
    void shouldThrowExceptionForTooLargeMonth() {
        // when & then
        assertThatThrownBy(() -> new GetMonthlyStatisticsQuery("user123", 2024, 13))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid month: 13");
    }

    @Test
    @DisplayName("경계값 테스트 - 유효한 최소 year")
    void shouldAcceptMinimumValidYear() {
        // when & then
        assertThatNoException().isThrownBy(() -> 
            new GetMonthlyStatisticsQuery("user123", 2020, 1));
    }

    @Test
    @DisplayName("경계값 테스트 - 유효한 최대 year")
    void shouldAcceptMaximumValidYear() {
        // when & then
        assertThatNoException().isThrownBy(() -> 
            new GetMonthlyStatisticsQuery("user123", 2100, 12));
    }
}