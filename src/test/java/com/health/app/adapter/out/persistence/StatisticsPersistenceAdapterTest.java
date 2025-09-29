package com.health.app.adapter.out.persistence;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatisticsPersistenceAdapter 테스트")
class StatisticsPersistenceAdapterTest {

    @Mock
    private DailyStatisticsJpaRepository dailyStatisticsRepository;

    @Mock
    private MonthlyStatisticsJpaRepository monthlyStatisticsRepository;

    private StatisticsPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new StatisticsPersistenceAdapter(
                dailyStatisticsRepository,
                monthlyStatisticsRepository);
    }

    @Test
    @DisplayName("일별 통계 로드 - 데이터가 존재하는 경우")
    void shouldLoadDailyStatisticsWhenDataExists() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        LocalDate date = LocalDate.of(2024, 3, 15);
        DailyStatisticsJpaEntity entity = createTestDailyStatisticsEntity();

        when(dailyStatisticsRepository.findByRecordKeyAndStatisticsDate("USER_123", date))
                .thenReturn(Optional.of(entity));

        // when
        Optional<DailyStatistics> result = adapter.loadDailyStatistics(recordKey, date);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getRecordKey().getValue()).isEqualTo("USER_123");
        verify(dailyStatisticsRepository).findByRecordKeyAndStatisticsDate("USER_123", date);
    }

    @Test
    @DisplayName("일별 통계 로드 - 데이터가 존재하지 않는 경우")
    void shouldLoadDailyStatisticsWhenDataNotExists() {
        // given
        RecordKey recordKey = new RecordKey("USER_456");
        LocalDate date = LocalDate.of(2024, 3, 15);

        when(dailyStatisticsRepository.findByRecordKeyAndStatisticsDate("USER_456", date))
                .thenReturn(Optional.empty());

        // when
        Optional<DailyStatistics> result = adapter.loadDailyStatistics(recordKey, date);

        // then
        assertThat(result).isEmpty();
        verify(dailyStatisticsRepository).findByRecordKeyAndStatisticsDate("USER_456", date);
    }

    @Test
    @DisplayName("월별 통계 로드 - 데이터가 존재하는 경우")
    void shouldLoadMonthlyStatisticsWhenDataExists() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        int year = 2024;
        int month = 3;
        MonthlyStatisticsJpaEntity entity = createTestMonthlyStatisticsEntity();

        when(monthlyStatisticsRepository.findByRecordKeyAndYearAndMonth("USER_123", year, month))
                .thenReturn(Optional.of(entity));

        // when
        Optional<MonthlyStatistics> result = adapter.loadMonthlyStatistics(recordKey, year, month);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getRecordKey().getValue()).isEqualTo("USER_123");
        assertThat(result.get().getYear()).isEqualTo(year);
        assertThat(result.get().getMonth()).isEqualTo(month);
        verify(monthlyStatisticsRepository).findByRecordKeyAndYearAndMonth("USER_123", year, month);
    }

    @Test
    @DisplayName("월별 통계 로드 - 데이터가 존재하지 않는 경우")
    void shouldLoadMonthlyStatisticsWhenDataNotExists() {
        // given
        RecordKey recordKey = new RecordKey("USER_456");
        int year = 2024;
        int month = 3;

        when(monthlyStatisticsRepository.findByRecordKeyAndYearAndMonth("USER_456", year, month))
                .thenReturn(Optional.empty());

        // when
        Optional<MonthlyStatistics> result = adapter.loadMonthlyStatistics(recordKey, year, month);

        // then
        assertThat(result).isEmpty();
        verify(monthlyStatisticsRepository).findByRecordKeyAndYearAndMonth("USER_456", year, month);
    }

    @Test
    @DisplayName("월별 일별 통계 목록 로드")
    void shouldLoadDailyStatisticsByMonth() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        int year = 2024;
        int month = 3;
        List<DailyStatisticsJpaEntity> entities = List.of(
                createTestDailyStatisticsEntity(),
                createTestDailyStatisticsEntity()
        );

        when(dailyStatisticsRepository.findByRecordKeyAndYearAndMonth("USER_123", year, month))
                .thenReturn(entities);

        // when
        List<DailyStatistics> result = adapter.loadDailyStatisticsByMonth(recordKey, year, month);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRecordKey().getValue()).isEqualTo("USER_123");
        verify(dailyStatisticsRepository).findByRecordKeyAndYearAndMonth("USER_123", year, month);
    }

    @Test
    @DisplayName("날짜 범위별 일별 통계 목록 로드")
    void shouldLoadDailyStatisticsByDateRange() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 31);
        List<DailyStatisticsJpaEntity> entities = List.of(
                createTestDailyStatisticsEntity(),
                createTestDailyStatisticsEntity()
        );

        when(dailyStatisticsRepository.findByRecordKeyAndStatisticsDateBetween("USER_123", startDate, endDate))
                .thenReturn(entities);

        // when
        List<DailyStatistics> result = adapter.loadDailyStatisticsByDateRange(recordKey, startDate, endDate);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRecordKey().getValue()).isEqualTo("USER_123");
        verify(dailyStatisticsRepository).findByRecordKeyAndStatisticsDateBetween("USER_123", startDate, endDate);
    }

    @Test
    @DisplayName("연도별 월별 통계 목록 로드")
    void shouldLoadMonthlyStatisticsByYear() {
        // given
        RecordKey recordKey = new RecordKey("USER_123");
        int year = 2024;
        List<MonthlyStatisticsJpaEntity> entities = List.of(
                createTestMonthlyStatisticsEntity(),
                createTestMonthlyStatisticsEntity()
        );

        when(monthlyStatisticsRepository.findByRecordKeyAndYearOrderByMonth("USER_123", year))
                .thenReturn(entities);

        // when
        List<MonthlyStatistics> result = adapter.loadMonthlyStatisticsByYear(recordKey, year);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRecordKey().getValue()).isEqualTo("USER_123");
        verify(monthlyStatisticsRepository).findByRecordKeyAndYearOrderByMonth("USER_123", year);
    }

    @Test
    @DisplayName("일별 통계 저장")
    void shouldSaveDailyStatistics() {
        // given
        DailyStatistics dailyStatistics = createTestDailyStatistics();
        DailyStatisticsJpaEntity savedEntity = createTestDailyStatisticsEntity();

        when(dailyStatisticsRepository.save(any(DailyStatisticsJpaEntity.class)))
                .thenReturn(savedEntity);

        // when
        DailyStatistics result = adapter.saveDailyStatistics(dailyStatistics);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRecordKey().getValue()).isEqualTo("USER_123");
        assertThat(result.getTotalSteps().getValue()).isEqualTo(10000);
        verify(dailyStatisticsRepository).save(any(DailyStatisticsJpaEntity.class));
    }

    @Test
    @DisplayName("월별 통계 저장")
    void shouldSaveMonthlyStatistics() {
        // given
        MonthlyStatistics monthlyStatistics = createTestMonthlyStatistics();
        MonthlyStatisticsJpaEntity savedEntity = createTestMonthlyStatisticsEntity();

        when(monthlyStatisticsRepository.save(any(MonthlyStatisticsJpaEntity.class)))
                .thenReturn(savedEntity);

        // when
        MonthlyStatistics result = adapter.saveMonthlyStatistics(monthlyStatistics);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getRecordKey().getValue()).isEqualTo("USER_123");
        assertThat(result.getYear()).isEqualTo(2024);
        assertThat(result.getMonth()).isEqualTo(3);
        assertThat(result.getTotalSteps().getValue()).isEqualTo(300000);
        verify(monthlyStatisticsRepository).save(any(MonthlyStatisticsJpaEntity.class));
    }

    @Test
    @DisplayName("빈 리스트 반환 - 월별 일별 통계")
    void shouldReturnEmptyListForDailyStatisticsByMonth() {
        // given
        RecordKey recordKey = new RecordKey("USER_789");
        when(dailyStatisticsRepository.findByRecordKeyAndYearAndMonth("USER_789", 2024, 3))
                .thenReturn(List.of());

        // when
        List<DailyStatistics> result = adapter.loadDailyStatisticsByMonth(recordKey, 2024, 3);

        // then
        assertThat(result).isEmpty();
        verify(dailyStatisticsRepository).findByRecordKeyAndYearAndMonth("USER_789", 2024, 3);
    }

    @Test
    @DisplayName("빈 리스트 반환 - 연도별 월별 통계")
    void shouldReturnEmptyListForMonthlyStatisticsByYear() {
        // given
        RecordKey recordKey = new RecordKey("USER_789");
        when(monthlyStatisticsRepository.findByRecordKeyAndYearOrderByMonth("USER_789", 2024))
                .thenReturn(List.of());

        // when
        List<MonthlyStatistics> result = adapter.loadMonthlyStatisticsByYear(recordKey, 2024);

        // then
        assertThat(result).isEmpty();
        verify(monthlyStatisticsRepository).findByRecordKeyAndYearOrderByMonth("USER_789", 2024);
    }

    // 테스트 헬퍼 메서드들
    private DailyStatisticsJpaEntity createTestDailyStatisticsEntity() {
        return new DailyStatisticsJpaEntity(
                "USER_123",
                LocalDate.of(2024, 3, 15),
                10000,
                BigDecimal.valueOf(500.00),
                BigDecimal.valueOf(7.500),
                LocalDateTime.of(2024, 3, 15, 10, 0),
                LocalDateTime.of(2024, 3, 15, 11, 0)
        );
    }

    private MonthlyStatisticsJpaEntity createTestMonthlyStatisticsEntity() {
        return new MonthlyStatisticsJpaEntity(
                "USER_123",
                2024,
                3,
                300000,
                BigDecimal.valueOf(15000.00),
                BigDecimal.valueOf(200.000),
                25,
                LocalDateTime.of(2024, 3, 31, 23, 59),
                LocalDateTime.of(2024, 4, 1, 0, 0)
        );
    }

    private DailyStatistics createTestDailyStatistics() {
        return new DailyStatistics(
                1L,
                new RecordKey("USER_123"),
                LocalDate.of(2024, 3, 15),
                new Steps(10000),
                new Calories(BigDecimal.valueOf(500.00)),
                new Distance(BigDecimal.valueOf(7.500)),
                LocalDateTime.of(2024, 3, 15, 10, 0),
                LocalDateTime.of(2024, 3, 15, 11, 0)
        );
    }

    private MonthlyStatistics createTestMonthlyStatistics() {
        return new MonthlyStatistics(
                1L,
                new RecordKey("USER_123"),
                2024,
                3,
                new Steps(300000),
                new Calories(BigDecimal.valueOf(15000.00)),
                new Distance(BigDecimal.valueOf(200.000)),
                25,
                LocalDateTime.of(2024, 3, 31, 23, 59),
                LocalDateTime.of(2024, 4, 1, 0, 0)
        );
    }
}