package com.health.app.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;
import com.health.app.application.port.out.LoadHealthDataPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.HealthData;
import com.health.app.util.JsonTestDataLoader;

/**
 * JSON 파일 기반 통합 테스트
 * 실제 JSON 파일에서 데이터를 로드하여 REST API 테스트
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("JSON 파일 기반 헬스 앱 데이터 API 통합 테스트")
class JsonFileBasedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoadHealthDataPort loadHealthDataPort;

    @Test
    @DisplayName("INPUT_DATA1.json으로 삼성헬스 아침 조깅 데이터 API 테스트")
    void testApi_WithInputData1Json_SamsungMorningJogging() throws Exception {
        // given - JSON 파일에서 데이터 로드
        SaveHealthDataRequest inputData1 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA1.json");

        // when & then - API 호출 및 응답 검증
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("SAMSUNG_USER_001"))
                .andExpect(jsonPath("$.steps").value(8250))
                .andExpect(jsonPath("$.calories").value(387.5))
                .andExpect(jsonPath("$.distance").value(5.2))
                .andExpect(jsonPath("$.collectedAt").value("2024-01-15T07:30:00"));

        // 데이터베이스 저장 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(savedData).hasSize(1);
        assertThat(savedData.get(0).getSteps().getValue()).isEqualTo(8250);
    }

    @Test
    @DisplayName("INPUT_DATA2.json으로 애플건강 출근길 데이터 API 테스트")
    void testApi_WithInputData2Json_AppleCommuteWalk() throws Exception {
        // given - JSON 파일에서 데이터 로드
        SaveHealthDataRequest inputData2 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA2.json");

        // when & then - API 호출 및 응답 검증
        mockMvc.perform(post("/api/v1/health-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputData2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordKey").value("APPLE_USER_002"))
                .andExpect(jsonPath("$.steps").value(3420))
                .andExpect(jsonPath("$.calories").value(158.3))
                .andExpect(jsonPath("$.distance").value(2.1));

        // 데이터베이스 저장 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(savedData).hasSize(1);
        assertThat(savedData.get(0).getCalories().getDoubleValue()).isEqualTo(158.3);
    }

    @Test
    @DisplayName("모든 JSON 파일로 배치 API 테스트")
    void testApi_WithAllJsonFiles_BatchProcessing() throws Exception {
        // given - 모든 JSON 파일에서 데이터 로드
        List<SaveHealthDataRequest> allData = JsonTestDataLoader.loadAllInputData();

        // when - 모든 데이터를 순차적으로 API 호출
        for (SaveHealthDataRequest data : allData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.recordKey").value(data.recordKey()))
                    .andExpect(jsonPath("$.steps").value(data.steps()));
        }

        // then - 데이터베이스에 모든 데이터가 저장되었는지 확인
        // 삼성헬스 사용자 데이터 확인
        List<HealthData> samsungData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(samsungData).hasSize(2); // INPUT_DATA1, INPUT_DATA3

        int samsungTotalSteps = samsungData.stream()
                .mapToInt(data -> data.getSteps().getValue())
                .sum();
        assertThat(samsungTotalSteps).isEqualTo(8250 + 2100); // 10,350걸음

        // 애플건강 사용자 데이터 확인
        List<HealthData> appleData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(appleData).hasSize(2); // INPUT_DATA2, INPUT_DATA4

        int appleTotalSteps = appleData.stream()
                .mapToInt(data -> data.getSteps().getValue())
                .sum();
        assertThat(appleTotalSteps).isEqualTo(3420 + 12500); // 15,920걸음
    }

    @Test
    @DisplayName("삼성헬스 데이터만 JSON 파일로 API 테스트")
    void testApi_WithSamsungHealthJsonData_OnlyDeviceType() throws Exception {
        // given - 삼성헬스 JSON 데이터만 로드
        List<SaveHealthDataRequest> samsungData = JsonTestDataLoader.loadSamsungHealthData();

        // when - 삼성헬스 데이터만 API 호출
        for (SaveHealthDataRequest data : samsungData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.recordKey").value("SAMSUNG_USER_001"));
        }

        // then - 삼성헬스 사용자만 데이터가 저장되었는지 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(savedData).hasSize(2);

        // 애플 사용자는 데이터가 없어야 함
        List<HealthData> appleData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(appleData).isEmpty();
    }

    @Test
    @DisplayName("애플건강 데이터만 JSON 파일로 API 테스트")
    void testApi_WithAppleHealthJsonData_OnlyDeviceType() throws Exception {
        // given - 애플건강 JSON 데이터만 로드
        List<SaveHealthDataRequest> appleData = JsonTestDataLoader.loadAppleHealthData();

        // when - 애플건강 데이터만 API 호출
        for (SaveHealthDataRequest data : appleData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.recordKey").value("APPLE_USER_002"));
        }

        // then - 애플건강 사용자만 데이터가 저장되었는지 확인
        List<HealthData> savedData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        assertThat(savedData).hasSize(2);

        // 삼성 사용자는 데이터가 없어야 함
        List<HealthData> samsungData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        assertThat(samsungData).isEmpty();
    }

    @Test
    @DisplayName("시간대별 JSON 데이터로 활동 패턴 API 테스트")
    void testApi_WithTimeBasedJsonData_ActivityPatterns() throws Exception {
        // given - 아침 시간대 데이터 (6시-10시)
        List<SaveHealthDataRequest> morningData = JsonTestDataLoader.loadDataByTimeRange(6, 10);

        // when - 아침 시간대 데이터 API 호출
        for (SaveHealthDataRequest data : morningData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated());
        }

        // then - 아침 시간대 활동 패턴 검증
        assertThat(morningData).hasSizeGreaterThan(0);
        for (SaveHealthDataRequest data : morningData) {
            assertThat(data.collectedAt().getHour()).isBetween(6, 10);
        }

        // 저장된 데이터 검증
        List<HealthData> samsungData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        List<HealthData> appleData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        
        List<HealthData> allSavedData = new ArrayList<>();
        allSavedData.addAll(samsungData);
        allSavedData.addAll(appleData);

        long morningActivities = allSavedData.stream()
                .filter(data -> data.getCollectedAt().getHour() >= 6 && data.getCollectedAt().getHour() <= 10)
                .count();
        
        assertThat(morningActivities).isEqualTo(morningData.size());
    }

    @Test
    @DisplayName("JSON 파일 데이터로 칼로리 소모량 분석 API 테스트")
    void testApi_WithJsonFiles_CalorieAnalysis() throws Exception {
        // given - 모든 JSON 데이터 로드
        List<SaveHealthDataRequest> allData = JsonTestDataLoader.loadAllInputData();

        // when - 모든 데이터 API 호출
        for (SaveHealthDataRequest data : allData) {
            mockMvc.perform(post("/api/v1/health-data")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(data)))
                    .andExpect(status().isCreated());
        }

        // then - 칼로리 소모량 분석
        List<HealthData> samsungData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("SAMSUNG_USER_001"));
        List<HealthData> appleData = loadHealthDataPort.loadHealthDataByRecordKey(
                new RecordKey("APPLE_USER_002"));
        
        List<HealthData> allSavedData = new ArrayList<>();
        allSavedData.addAll(samsungData);
        allSavedData.addAll(appleData);

        // 고강도 운동 (700kcal 이상)
        long highIntensityWorkouts = allSavedData.stream()
                .filter(data -> data.getCalories().getDoubleValue() >= 700)
                .count();
        assertThat(highIntensityWorkouts).isEqualTo(1); // INPUT_DATA4

        // 중강도 운동 (200-699kcal)
        long mediumIntensityWorkouts = allSavedData.stream()
                .filter(data -> data.getCalories().getDoubleValue() >= 200 && data.getCalories().getDoubleValue() < 700)
                .count();
        assertThat(mediumIntensityWorkouts).isEqualTo(1); // INPUT_DATA1

        // 저강도 활동 (200kcal 미만)
        long lowIntensityActivities = allSavedData.stream()
                .filter(data -> data.getCalories().getDoubleValue() < 200)
                .count();
        assertThat(lowIntensityActivities).isEqualTo(2); // INPUT_DATA2, INPUT_DATA3
    }
}