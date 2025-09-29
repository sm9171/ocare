package com.health.app.scenario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;
import com.health.app.application.port.out.LoadHealthDataPort;
import com.health.app.application.port.out.LoadStatisticsPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.HealthData;
import com.health.app.fixture.HealthAppDataFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("헬스 앱 데이터 수집 시나리오 테스트")
class HealthAppDataScenarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoadHealthDataPort loadHealthDataPort;

    @Autowired
    private LoadStatisticsPort loadStatisticsPort;

    @Test
    @DisplayName("시나리오 1: 삼성헬스 사용자의 하루 활동 패턴")
    void scenario1_SamsungHealthUser_DailyActivityPattern() throws Exception {

        // when - 하루 종일 활동 데이터 수집
        // 1) 아침 7:30 - 조깅 (INPUT_DATA1)
        SaveHealthDataRequest morningJogging = HealthAppDataFixture.INPUT_DATA1;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(morningJogging)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("SAMSUNG_USER_001"))
                .andExpect(jsonPath("$.steps").value(8250))
                .andExpect(jsonPath("$.calories").value(387.5));

        // 2) 점심 12:30 - 산책 (INPUT_DATA3)
        SaveHealthDataRequest lunchWalk = HealthAppDataFixture.INPUT_DATA3;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lunchWalk)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.steps").value(2100))
                .andExpect(jsonPath("$.calories").value(95.7));

        // then - 하루 총 활동량 검증
        List<HealthData> dailyData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("SAMSUNG_USER_001"), LocalDate.of(2024, 1, 15));

        assertThat(dailyData).hasSize(2);

        int totalSteps = dailyData.stream().mapToInt(data -> data.getSteps().getValue()).sum();
        double totalCalories = dailyData.stream().mapToDouble(data -> data.getCalories().getDoubleValue()).sum();
        double totalDistance = dailyData.stream().mapToDouble(data -> data.getDistance().getDoubleValue()).sum();

        assertThat(totalSteps).isEqualTo(10350); // 8250 + 2100
        assertThat(totalCalories).isEqualTo(483.2); // 387.5 + 95.7
        assertThat(totalDistance).isEqualTo(6.5); // 5.2 + 1.3

        // 일별 통계 조회
        mockMvc.perform(get("/api/v1/statistics/daily")
                        .param("recordKey", "SAMSUNG_USER_001")
                        .param("date", "2024-01-15"))
                .andExpect(status().isNotFound()); // 아직 집계되지 않음
    }

    @Test
    @DisplayName("시나리오 2: 애플건강 사용자의 출근-운동-퇴근 패턴")
    void scenario2_AppleHealthUser_CommuteWorkoutPattern() throws Exception {
        // when - 출근-운동-퇴근 패턴 데이터 수집
        // 1) 아침 8:45 - 출근길 걷기 (INPUT_DATA2)
        SaveHealthDataRequest commuteWalk = HealthAppDataFixture.INPUT_DATA2;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commuteWalk)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("APPLE_USER_002"));

        // 2) 저녁 7:15 - 헬스장 운동 후 귀가 (INPUT_DATA4)
        SaveHealthDataRequest eveningWorkout = HealthAppDataFixture.INPUT_DATA4;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eveningWorkout)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.steps").value(12500))
                .andExpect(jsonPath("$.calories").value(742.8));

        // then - 활동 패턴 분석
        List<HealthData> dailyData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("APPLE_USER_002"), LocalDate.of(2024, 1, 15));

        assertThat(dailyData).hasSize(2);

        // 출근길 데이터 검증
        HealthData morningData = dailyData.stream()
                .filter(data -> data.getCollectedAt().getHour() == 8)
                .findFirst().orElseThrow();
        assertThat(morningData.getSteps().getValue()).isEqualTo(3420);

        // 저녁 운동 데이터 검증 (고강도)
        HealthData eveningData = dailyData.stream()
                .filter(data -> data.getCollectedAt().getHour() == 19)
                .findFirst().orElseThrow();
        assertThat(eveningData.getSteps().getValue()).isEqualTo(12500);
        assertThat(eveningData.getCalories().getDoubleValue()).isGreaterThan(700); // 고강도 운동
    }

    @Test
    @DisplayName("시나리오 3: 주말 고강도 운동 데이터 수집")
    void scenario3_WeekendHighIntensityWorkout() throws Exception {
        // when - 주말 하이킹 데이터 수집
        SaveHealthDataRequest hikingData = HealthAppDataFixture.WEEKEND_HIKING_DATA;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hikingData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.steps").value(15680))
                .andExpect(jsonPath("$.calories").value(892.4))
                .andExpect(jsonPath("$.distance").value(11.2));

        // then - 고강도 운동 데이터 검증
        List<HealthData> weekendData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("SAMSUNG_USER_001"), LocalDate.of(2024, 1, 20));

        assertThat(weekendData).hasSize(1);
        HealthData hiking = weekendData.get(0);

        // 고강도 운동 기준 검증
        assertThat(hiking.getSteps().getValue()).isGreaterThan(15000);
        assertThat(hiking.getCalories().getDoubleValue()).isGreaterThan(800);
        assertThat(hiking.getDistance().getDoubleValue()).isGreaterThan(10);
        assertThat(hiking.getCollectedAt().getDayOfWeek().getValue()).isEqualTo(6); // 토요일
    }

    @Test
    @DisplayName("시나리오 4: 새벽 러닝족의 일요일 운동")
    void scenario4_EarlyMorningRunner_SundayWorkout() throws Exception {
        // when - 새벽 6시 러닝 데이터 수집
        SaveHealthDataRequest earlyRunning = HealthAppDataFixture.EARLY_RUNNING_DATA;
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(earlyRunning)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.collectedAt").value("2024-01-21T06:00:00"));

        // then - 새벽 운동 패턴 검증
        List<HealthData> sundayData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("APPLE_USER_002"), LocalDate.of(2024, 1, 21));

        assertThat(sundayData).hasSize(1);
        HealthData earlyRun = sundayData.get(0);

        assertThat(earlyRun.getCollectedAt().getHour()).isEqualTo(6); // 새벽 6시
        assertThat(earlyRun.getCollectedAt().getDayOfWeek().getValue()).isEqualTo(7); // 일요일
        assertThat(earlyRun.getSteps().getValue()).isEqualTo(6780);
        assertThat(earlyRun.getCalories().getDoubleValue()).isEqualTo(456.2);
    }

    @Test
    @DisplayName("시나리오 5: 한 주간의 활동 패턴 종합 분석")
    void scenario5_WeeklyActivityPatternAnalysis() throws Exception {
        // given - 일주일간의 다양한 활동 데이터 수집
        List<SaveHealthDataRequest> weeklyData = HealthAppDataFixture.getAllTestData();

        // when - 모든 데이터 순차적으로 수집
        for (SaveHealthDataRequest data : weeklyData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated());
        }

        // then - 주간 활동 패턴 분석
        // 1) 삼성헬스 사용자 활동 분석
        List<HealthData> samsungWeeklyData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(samsungWeeklyData).hasSize(3); // INPUT_DATA1, INPUT_DATA3, WEEKEND_HIKING_DATA

        int samsungTotalSteps = samsungWeeklyData.stream()
                .mapToInt(data -> data.getSteps().getValue()).sum();
        assertThat(samsungTotalSteps).isEqualTo(8250 + 2100 + 15680); // 26,030걸음

        // 2) 애플건강 사용자 활동 분석
        List<HealthData> appleWeeklyData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(appleWeeklyData).hasSize(3); // INPUT_DATA2, INPUT_DATA4, EARLY_RUNNING_DATA

        int appleTotalSteps = appleWeeklyData.stream()
                .mapToInt(data -> data.getSteps().getValue()).sum();
        assertThat(appleTotalSteps).isEqualTo(3420 + 12500 + 6780); // 22,700걸음

        // 3) 활동 시간대 분석
        long morningWorkouts = weeklyData.stream()
                .filter(data -> data.collectedAt() != null)
                .filter(data -> data.collectedAt().getHour() <= 8)
                .count();
        assertThat(morningWorkouts).isGreaterThan(0); // 새벽/아침 운동족 존재

        long eveningWorkouts = weeklyData.stream()
                .filter(data -> data.collectedAt() != null)
                .filter(data -> data.collectedAt().getHour() >= 18)
                .count();
        assertThat(eveningWorkouts).isGreaterThan(0); // 저녁 운동족 존재
    }

    @Test
    @DisplayName("시나리오 6: 데이터 검증 및 오류 처리")
    void scenario6_DataValidationAndErrorHandling() throws Exception {
        // when & then - 다양한 잘못된 데이터에 대한 검증
        
        // 1) 음수 걸음 수
        SaveHealthDataRequest invalidSteps = new SaveHealthDataRequest(
                "TEST_USER", -1000, java.math.BigDecimal.valueOf(100), 
                java.math.BigDecimal.valueOf(1.0), null);
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSteps)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));

        // 2) 음수 칼로리
        SaveHealthDataRequest invalidCalories = new SaveHealthDataRequest(
                "TEST_USER", 5000, java.math.BigDecimal.valueOf(-100), 
                java.math.BigDecimal.valueOf(1.0), null);
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCalories)))
                .andExpect(status().isBadRequest());

        // 3) 빈 RecordKey
        SaveHealthDataRequest emptyRecordKey = new SaveHealthDataRequest(
                "", 5000, java.math.BigDecimal.valueOf(100), 
                java.math.BigDecimal.valueOf(1.0), null);
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRecordKey)))
                .andExpect(status().isBadRequest());

        // 4) 존재하지 않는 통계 조회
        mockMvc.perform(get("/api/v1/statistics/daily")
                        .param("recordKey", "NON_EXISTENT_USER")
                        .param("date", "2024-01-01"))
                .andExpect(status().isNotFound());
    }
}