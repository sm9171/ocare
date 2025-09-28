package com.health.app.domain.health;

import com.health.app.domain.common.RecordKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("헬스 앱 데이터 통계 집계 테스트")
class HealthAppDataAggregationTest {

    @Test
    @DisplayName("삼성헬스 사용자의 하루 데이터 집계 - INPUT_DATA1, INPUT_DATA3")
    void aggregateDailyStatistics_SamsungUser_Success() {
        // given - 삼성헬스 사용자의 하루 데이터
        RecordKey samsungUser = new RecordKey("SAMSUNG_USER_001");
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // INPUT_DATA1: 아침 조깅 데이터
        HealthData morningJogging = new HealthData(
                samsungUser,
                new Steps(8250),
                new Calories(387.5),
                new Distance(5.2),
                LocalDateTime.of(2024, 1, 15, 7, 30)
        );

        // INPUT_DATA3: 점심시간 산책 데이터
        HealthData lunchWalk = new HealthData(
                samsungUser,
                new Steps(2100),
                new Calories(95.7),
                new Distance(1.3),
                LocalDateTime.of(2024, 1, 15, 12, 30)
        );

        List<HealthData> healthDataList = List.of(morningJogging, lunchWalk);

        // when - 일별 통계 집계
        DailyStatistics dailyStats = DailyStatistics.aggregate(samsungUser, targetDate, healthDataList);

        // then - 집계 결과 검증
        assertThat(dailyStats.getRecordKey()).isEqualTo(samsungUser);
        assertThat(dailyStats.getStatisticsDate()).isEqualTo(targetDate);
        assertThat(dailyStats.getTotalSteps().getValue()).isEqualTo(8250 + 2100); // 10,350걸음
        assertThat(dailyStats.getTotalCalories().getDoubleValue()).isEqualTo(387.5 + 95.7); // 483.2 kcal
        assertThat(dailyStats.getTotalDistance().getDoubleValue()).isEqualTo(5.2 + 1.3); // 6.5 km
    }

    @Test
    @DisplayName("애플건강 사용자의 하루 데이터 집계 - INPUT_DATA2, INPUT_DATA4")
    void aggregateDailyStatistics_AppleUser_Success() {
        // given - 애플건강 사용자의 하루 데이터
        RecordKey appleUser = new RecordKey("APPLE_USER_002");
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // INPUT_DATA2: 출근길 걷기 데이터
        HealthData commuteWalk = new HealthData(
                appleUser,
                new Steps(3420),
                new Calories(158.3),
                new Distance(2.1),
                LocalDateTime.of(2024, 1, 15, 8, 45)
        );

        // INPUT_DATA4: 저녁 운동 데이터
        HealthData eveningWorkout = new HealthData(
                appleUser,
                new Steps(12500),
                new Calories(742.8),
                new Distance(8.7),
                LocalDateTime.of(2024, 1, 15, 19, 15)
        );

        List<HealthData> healthDataList = List.of(commuteWalk, eveningWorkout);

        // when - 일별 통계 집계
        DailyStatistics dailyStats = DailyStatistics.aggregate(appleUser, targetDate, healthDataList);

        // then - 집계 결과 검증
        assertThat(dailyStats.getTotalSteps().getValue()).isEqualTo(3420 + 12500); // 15,920걸음
        assertThat(dailyStats.getTotalCalories().getDoubleValue()).isCloseTo(158.3 + 742.8, within(0.1)); // 901.1 kcal
        assertThat(dailyStats.getTotalDistance().getDoubleValue()).isCloseTo(2.1 + 8.7, within(0.1)); // 10.8 km
    }

    @Test
    @DisplayName("월별 통계 집계 - 여러 일별 통계로부터")
    void aggregateMonthlyStatistics_MultipleDaily_Success() {
        // given - 한 달 동안의 일별 통계 데이터
        RecordKey user = new RecordKey("SAMSUNG_USER_001");
        int year = 2024;
        int month = 1;

        // 1월 15일 통계 (INPUT_DATA1 + INPUT_DATA3)
        DailyStatistics day15Stats = new DailyStatistics(
                1L, user, LocalDate.of(2024, 1, 15),
                new Steps(10350), new Calories(483.2), new Distance(6.5),
                LocalDateTime.now(), LocalDateTime.now()
        );

        // 1월 20일 통계 (주말 하이킹)
        DailyStatistics day20Stats = new DailyStatistics(
                2L, user, LocalDate.of(2024, 1, 20),
                new Steps(15680), new Calories(892.4), new Distance(11.2),
                LocalDateTime.now(), LocalDateTime.now()
        );

        // 1월 22일 통계 (가벼운 산책)
        DailyStatistics day22Stats = new DailyStatistics(
                3L, user, LocalDate.of(2024, 1, 22),
                new Steps(5400), new Calories(245.8), new Distance(3.8),
                LocalDateTime.now(), LocalDateTime.now()
        );

        List<DailyStatistics> dailyStatsList = List.of(day15Stats, day20Stats, day22Stats);

        // when - 월별 통계 집계
        MonthlyStatistics monthlyStats = MonthlyStatistics.aggregate(user, year, month, dailyStatsList);

        // then - 집계 결과 검증
        assertThat(monthlyStats.getRecordKey()).isEqualTo(user);
        assertThat(monthlyStats.getYear()).isEqualTo(2024);
        assertThat(monthlyStats.getMonth()).isEqualTo(1);
        assertThat(monthlyStats.getTotalSteps().getValue()).isEqualTo(10350 + 15680 + 5400); // 31,430걸음
        assertThat(monthlyStats.getTotalCalories().getDoubleValue()).isCloseTo(483.2 + 892.4 + 245.8, within(0.1)); // 1,621.4 kcal
        assertThat(monthlyStats.getTotalDistance().getDoubleValue()).isCloseTo(6.5 + 11.2 + 3.8, within(0.1)); // 21.5 km
        assertThat(monthlyStats.getActiveDays()).isEqualTo(3); // 3일 활동

        // 평균 계산 검증
        assertThat(monthlyStats.getAverageStepsPerDay().getValue()).isEqualTo(31430 / 3); // 일평균 걸음 수
        assertThat(monthlyStats.getAverageCaloriesPerDay().getDoubleValue())
                .isCloseTo(1621.4 / 3, within(0.1)); // 일평균 칼로리
        assertThat(monthlyStats.getAverageDistancePerDay().getDoubleValue())
                .isCloseTo(21.5 / 3, within(0.1)); // 일평균 거리
    }

    @Test
    @DisplayName("고강도 운동일의 데이터 집계 - 주말 하이킹")
    void aggregateDailyStatistics_HighIntensityDay_Success() {
        // given - 고강도 운동 데이터
        RecordKey user = new RecordKey("SAMSUNG_USER_001");
        LocalDate hikingDay = LocalDate.of(2024, 1, 20);

        HealthData hikingData = new HealthData(
                user,
                new Steps(15680),
                new Calories(892.4),
                new Distance(11.2),
                LocalDateTime.of(2024, 1, 20, 14, 30)
        );

        List<HealthData> healthDataList = List.of(hikingData);

        // when
        DailyStatistics dailyStats = DailyStatistics.aggregate(user, hikingDay, healthDataList);

        // then - 고강도 운동 통계 검증
        assertThat(dailyStats.getTotalSteps().getValue()).isGreaterThan(15000);
        assertThat(dailyStats.getTotalCalories().getDoubleValue()).isGreaterThan(800);
        assertThat(dailyStats.getTotalDistance().getDoubleValue()).isGreaterThan(10);
    }

    @Test
    @DisplayName("저강도 활동일의 데이터 집계 - 가벼운 산책")
    void aggregateDailyStatistics_LowIntensityDay_Success() {
        // given - 저강도 활동 데이터
        RecordKey user = new RecordKey("APPLE_USER_002");
        LocalDate lightDay = LocalDate.of(2024, 1, 25);

        HealthData lightWalk = new HealthData(
                user,
                new Steps(2800),
                new Calories(125.5),
                new Distance(1.8),
                LocalDateTime.of(2024, 1, 25, 18, 0)
        );

        List<HealthData> healthDataList = List.of(lightWalk);

        // when
        DailyStatistics dailyStats = DailyStatistics.aggregate(user, lightDay, healthDataList);

        // then - 저강도 활동 통계 검증
        assertThat(dailyStats.getTotalSteps().getValue()).isLessThan(5000);
        assertThat(dailyStats.getTotalCalories().getDoubleValue()).isLessThan(200);
        assertThat(dailyStats.getTotalDistance().getDoubleValue()).isLessThan(3);
    }

    @Test
    @DisplayName("서로 다른 시간대의 데이터 집계 - 새벽부터 저녁까지")
    void aggregateDailyStatistics_DifferentTimeSlots_Success() {
        // given - 하루 종일 다양한 시간대의 데이터
        RecordKey user = new RecordKey("MULTI_TIME_USER");
        LocalDate fullDay = LocalDate.of(2024, 1, 30);

        // 새벽 6시 러닝
        HealthData earlyRunning = new HealthData(
                user, new Steps(6780), new Calories(456.2), new Distance(4.8),
                LocalDateTime.of(2024, 1, 30, 6, 0)
        );

        // 점심 12시 산책
        HealthData lunchWalk = new HealthData(
                user, new Steps(1500), new Calories(68.3), new Distance(0.9),
                LocalDateTime.of(2024, 1, 30, 12, 0)
        );

        // 저녁 7시 헬스장
        HealthData eveningGym = new HealthData(
                user, new Steps(4200), new Calories(315.7), new Distance(2.8),
                LocalDateTime.of(2024, 1, 30, 19, 0)
        );

        List<HealthData> healthDataList = List.of(earlyRunning, lunchWalk, eveningGym);

        // when
        DailyStatistics dailyStats = DailyStatistics.aggregate(user, fullDay, healthDataList);

        // then - 하루 종일 활동 통계 검증
        assertThat(dailyStats.getTotalSteps().getValue()).isEqualTo(6780 + 1500 + 4200); // 12,480걸음
        assertThat(dailyStats.getTotalCalories().getDoubleValue()).isEqualTo(456.2 + 68.3 + 315.7); // 840.2 kcal
        assertThat(dailyStats.getTotalDistance().getDoubleValue()).isEqualTo(4.8 + 0.9 + 2.8); // 8.5 km

        // 다양한 시간대에 골고루 활동했는지 확인
        assertThat(healthDataList.get(0).getCollectedAt().getHour()).isEqualTo(6); // 새벽
        assertThat(healthDataList.get(1).getCollectedAt().getHour()).isEqualTo(12); // 점심
        assertThat(healthDataList.get(2).getCollectedAt().getHour()).isEqualTo(19); // 저녁
    }
}