package com.health.app.integration;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("헬스 앱 데이터 수집 통합 테스트")
class HealthAppDataCollectionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoadHealthDataPort loadHealthDataPort;

    @Autowired
    private LoadStatisticsPort loadStatisticsPort;

    @Test
    @DisplayName("삼성헬스 데이터 수집 - INPUT_DATA1 (아침 조깅)")
    void collectSamsungHealthData_MorningJogging_Success() throws Exception {
        // given
        SaveHealthDataRequest inputData1 = HealthAppDataFixture.INPUT_DATA1;

        // when & then
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("SAMSUNG_USER_001"))
                .andExpect(jsonPath("$.steps").value(8250))
                .andExpect(jsonPath("$.calories").value(387.5))
                .andExpect(jsonPath("$.distance").value(5.2))
                .andExpect(jsonPath("$.collectedAt").value("2024-01-15T07:30:00"));

        // 데이터베이스에 저장되었는지 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(savedData).hasSize(1);
        assertThat(savedData.get(0).getSteps().getValue()).isEqualTo(8250);
    }

    @Test
    @DisplayName("애플건강 데이터 수집 - INPUT_DATA2 (출근길 걷기)")
    void collectAppleHealthData_CommuteWalking_Success() throws Exception {
        // given
        SaveHealthDataRequest inputData2 = HealthAppDataFixture.INPUT_DATA2;

        // when & then
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("APPLE_USER_002"))
                .andExpect(jsonPath("$.steps").value(3420))
                .andExpect(jsonPath("$.calories").value(158.3))
                .andExpect(jsonPath("$.distance").value(2.1));

        // 데이터베이스에 저장되었는지 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(savedData).hasSize(1);
        assertThat(savedData.get(0).getCalories().getDoubleValue()).isEqualTo(158.3);
    }

    @Test
    @DisplayName("같은 사용자의 하루 종일 데이터 수집 - INPUT_DATA1, INPUT_DATA3")
    void collectMultipleDataSameUser_AllDay_Success() throws Exception {
        // given - 같은 사용자(SAMSUNG_USER_001)의 아침, 점심 데이터
        SaveHealthDataRequest morningData = HealthAppDataFixture.INPUT_DATA1;
        SaveHealthDataRequest lunchData = HealthAppDataFixture.INPUT_DATA3;

        // when - 아침 데이터 저장
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(morningData)))
                .andExpect(status().isCreated());

        // 점심 데이터 저장
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lunchData)))
                .andExpect(status().isCreated());

        // then - 두 개의 데이터가 모두 저장되었는지 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("SAMSUNG_USER_001"), LocalDate.of(2024, 1, 15));
        
        assertThat(savedData).hasSize(2);
        
        // 총 걸음 수 합계 확인
        int totalSteps = savedData.stream()
                .mapToInt(data -> data.getSteps().getValue())
                .sum();
        assertThat(totalSteps).isEqualTo(8250 + 2100); // 10,350걸음

        // 총 칼로리 합계 확인
        double totalCalories = savedData.stream()
                .mapToDouble(data -> data.getCalories().getDoubleValue())
                .sum();
        assertThat(totalCalories).isEqualTo(387.5 + 95.7); // 483.2 kcal
    }

    @Test
    @DisplayName("하루 종일 모든 데이터 수집 - INPUT_DATA1~4 (두 사용자)")
    void collectAllDayData_TwoUsers_Success() throws Exception {
        // given - 하루 동안의 모든 데이터
        List<SaveHealthDataRequest> allDayData = HealthAppDataFixture.getSameDayData();

        // when - 모든 데이터 저장
        for (SaveHealthDataRequest data : allDayData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated());
        }

        // then - 삼성헬스 사용자 데이터 확인
        List<HealthData> samsungUserData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("SAMSUNG_USER_001"), LocalDate.of(2024, 1, 15));
        assertThat(samsungUserData).hasSize(2); // INPUT_DATA1, INPUT_DATA3
        
        int samsungTotalSteps = samsungUserData.stream()
                .mapToInt(data -> data.getSteps().getValue())
                .sum();
        assertThat(samsungTotalSteps).isEqualTo(8250 + 2100); // 10,350걸음

        // 애플건강 사용자 데이터 확인
        List<HealthData> appleUserData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("APPLE_USER_002"), LocalDate.of(2024, 1, 15));
        assertThat(appleUserData).hasSize(2); // INPUT_DATA2, INPUT_DATA4
        
        int appleTotalSteps = appleUserData.stream()
                .mapToInt(data -> data.getSteps().getValue())
                .sum();
        assertThat(appleTotalSteps).isEqualTo(3420 + 12500); // 15,920걸음
    }

    @Test
    @DisplayName("잘못된 데이터 검증 - 음수 걸음 수")
    void collectInvalidData_NegativeSteps_BadRequest() throws Exception {
        // given - 잘못된 데이터 (음수 걸음 수)
        SaveHealthDataRequest invalidData = new SaveHealthDataRequest(
                "INVALID_USER",
                -1000, // 음수 걸음 수
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1.0),
                null
        );

        // when & then
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("주말 하이킹 데이터 수집 - 고강도 운동")
    void collectWeekendHikingData_HighIntensity_Success() throws Exception {
        // given
        SaveHealthDataRequest hikingData = HealthAppDataFixture.WEEKEND_HIKING_DATA;

        // when & then
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hikingData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.steps").value(15680))
                .andExpect(jsonPath("$.calories").value(892.4))
                .andExpect(jsonPath("$.distance").value(11.2));

        // 고강도 운동 데이터 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(savedData).hasSize(1);
        
        HealthData highIntensityData = savedData.get(0);
        assertThat(highIntensityData.getSteps().getValue()).isGreaterThan(15000);
        assertThat(highIntensityData.getCalories().getDoubleValue()).isGreaterThan(800);
        assertThat(highIntensityData.getDistance().getDoubleValue()).isGreaterThan(10);
    }

    @Test
    @DisplayName("새벽 러닝 데이터 수집 - 이른 시간 운동")
    void collectEarlyRunningData_EarlyMorning_Success() throws Exception {
        // given
        SaveHealthDataRequest runningData = HealthAppDataFixture.EARLY_RUNNING_DATA;

        // when & then
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(runningData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.collectedAt").value("2024-01-21T06:00:00"));

        // 새벽 시간 데이터 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKeyAndDate(
                new RecordKey("APPLE_USER_002"), LocalDate.of(2024, 1, 21));
        assertThat(savedData).hasSize(1);
        assertThat(savedData.get(0).getCollectedAt().getHour()).isEqualTo(6);
    }
}