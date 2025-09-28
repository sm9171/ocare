package com.health.app.json;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;
import com.health.app.application.port.in.SaveHealthDataCommand;
import com.health.app.application.port.out.SaveHealthDataPort;
import com.health.app.application.service.HealthDataService;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.HealthData;
import com.health.app.domain.health.Steps;

@DisplayName("JSON 파일 기반 헬스 앱 데이터 생성 테스트")
class JsonHealthDataTest {

    @Mock
    private SaveHealthDataPort saveHealthDataPort;

    private HealthDataService healthDataService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        healthDataService = new HealthDataService(saveHealthDataPort);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("INPUT_DATA1.json 파일로부터 삼성헬스 데이터 생성")
    void createHealthData_FromInputData1Json_Success() throws IOException {
        // given - JSON 파일 로드
        SaveHealthDataRequest request = loadJsonFile("json/INPUT_DATA1.json");
        
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                request.recordKey(),
                request.steps(),
                request.calories(),
                request.distance(),
                request.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("SAMSUNG_USER_001"),
                new Steps(8250),
                new Calories(387.5),
                new Distance(5.2),
                LocalDateTime.of(2024, 1, 15, 7, 30)
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 삼성헬스 아침 조깅 데이터 검증
        assertThat(result.getRecordKey().getValue()).isEqualTo("SAMSUNG_USER_001");
        assertThat(result.getSteps().getValue()).isEqualTo(8250);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(387.5);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(5.2);
        assertThat(result.getCollectedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 7, 30));

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("INPUT_DATA2.json 파일로부터 애플건강 데이터 생성")
    void createHealthData_FromInputData2Json_Success() throws IOException {
        // given - JSON 파일 로드
        SaveHealthDataRequest request = loadJsonFile("json/INPUT_DATA2.json");
        
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                request.recordKey(),
                request.steps(),
                request.calories(),
                request.distance(),
                request.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("APPLE_USER_002"),
                new Steps(3420),
                new Calories(158.3),
                new Distance(2.1),
                LocalDateTime.of(2024, 1, 15, 8, 45)
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 애플건강 출근길 걷기 데이터 검증
        assertThat(result.getRecordKey().getValue()).isEqualTo("APPLE_USER_002");
        assertThat(result.getSteps().getValue()).isEqualTo(3420);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(158.3);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(2.1);
        assertThat(result.getCollectedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 8, 45));

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("INPUT_DATA3.json 파일로부터 점심시간 산책 데이터 생성")
    void createHealthData_FromInputData3Json_Success() throws IOException {
        // given - JSON 파일 로드
        SaveHealthDataRequest request = loadJsonFile("json/INPUT_DATA3.json");
        
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                request.recordKey(),
                request.steps(),
                request.calories(),
                request.distance(),
                request.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("SAMSUNG_USER_001"),
                new Steps(2100),
                new Calories(95.7),
                new Distance(1.3),
                LocalDateTime.of(2024, 1, 15, 12, 30)
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 삼성헬스 점심시간 산책 데이터 검증
        assertThat(result.getRecordKey().getValue()).isEqualTo("SAMSUNG_USER_001");
        assertThat(result.getSteps().getValue()).isEqualTo(2100);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(95.7);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(1.3);
        assertThat(result.getCollectedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 12, 30));

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("INPUT_DATA4.json 파일로부터 저녁 운동 데이터 생성")
    void createHealthData_FromInputData4Json_Success() throws IOException {
        // given - JSON 파일 로드
        SaveHealthDataRequest request = loadJsonFile("json/INPUT_DATA4.json");
        
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                request.recordKey(),
                request.steps(),
                request.calories(),
                request.distance(),
                request.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("APPLE_USER_002"),
                new Steps(12500),
                new Calories(742.8),
                new Distance(8.7),
                LocalDateTime.of(2024, 1, 15, 19, 15)
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 애플건강 저녁 운동 데이터 검증
        assertThat(result.getRecordKey().getValue()).isEqualTo("APPLE_USER_002");
        assertThat(result.getSteps().getValue()).isEqualTo(12500);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(742.8);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(8.7);
        assertThat(result.getCollectedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 19, 15));

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("모든 JSON 파일로부터 데이터 배치 생성 및 집계 테스트")
    void createHealthData_FromAllJsonFiles_BatchProcessing() throws IOException {
        // given - 모든 JSON 파일 로드
        List<String> jsonFiles = List.of(
                "json/INPUT_DATA1.json",
                "json/INPUT_DATA2.json", 
                "json/INPUT_DATA3.json",
                "json/INPUT_DATA4.json"
        );

        // when & then - 각 JSON 파일로부터 데이터 생성 및 검증
        for (String jsonFile : jsonFiles) {
            SaveHealthDataRequest request = loadJsonFile(jsonFile);
            
            // JSON 데이터 검증
            assertThat(request.recordKey()).isNotNull().isNotEmpty();
            assertThat(request.steps()).isGreaterThan(0);
            assertThat(request.calories()).isGreaterThan(BigDecimal.ZERO);
            assertThat(request.distance()).isGreaterThan(BigDecimal.ZERO);
            assertThat(request.collectedAt()).isNotNull();
            
            // 2024-01-15 날짜 검증
            assertThat(request.collectedAt().toLocalDate().toString()).isEqualTo("2024-01-15");
        }

        // 삼성헬스 사용자 데이터 합계 검증 (INPUT_DATA1, INPUT_DATA3)
        SaveHealthDataRequest samsung1 = loadJsonFile("json/INPUT_DATA1.json");
        SaveHealthDataRequest samsung2 = loadJsonFile("json/INPUT_DATA3.json");
        
        int samsungTotalSteps = samsung1.steps() + samsung2.steps();
        double samsungTotalCalories = samsung1.calories().doubleValue() + samsung2.calories().doubleValue();
        double samsungTotalDistance = samsung1.distance().doubleValue() + samsung2.distance().doubleValue();
        
        assertThat(samsungTotalSteps).isEqualTo(8250 + 2100); // 10,350걸음
        assertThat(samsungTotalCalories).isCloseTo(387.5 + 95.7, within(0.1)); // 483.2 kcal
        assertThat(samsungTotalDistance).isCloseTo(5.2 + 1.3, within(0.1)); // 6.5 km

        // 애플건강 사용자 데이터 합계 검증 (INPUT_DATA2, INPUT_DATA4)
        SaveHealthDataRequest apple1 = loadJsonFile("json/INPUT_DATA2.json");
        SaveHealthDataRequest apple2 = loadJsonFile("json/INPUT_DATA4.json");
        
        int appleTotalSteps = apple1.steps() + apple2.steps();
        double appleTotalCalories = apple1.calories().doubleValue() + apple2.calories().doubleValue();
        double appleTotalDistance = apple1.distance().doubleValue() + apple2.distance().doubleValue();
        
        assertThat(appleTotalSteps).isEqualTo(3420 + 12500); // 15,920걸음
        assertThat(appleTotalCalories).isCloseTo(158.3 + 742.8, within(0.1)); // 901.1 kcal
        assertThat(appleTotalDistance).isCloseTo(2.1 + 8.7, within(0.1)); // 10.8 km
    }

    @Test
    @DisplayName("JSON 파일 데이터 구조 및 타입 검증")
    void validateJsonFileStructure_AllFiles_Success() throws IOException {
        // given - 모든 JSON 파일
        List<String> jsonFiles = List.of(
                "json/INPUT_DATA1.json",
                "json/INPUT_DATA2.json", 
                "json/INPUT_DATA3.json",
                "json/INPUT_DATA4.json"
        );

        // when & then - 각 파일의 데이터 구조 검증
        for (String jsonFile : jsonFiles) {
            SaveHealthDataRequest request = loadJsonFile(jsonFile);
            
            // 필수 필드 존재 검증
            assertThat(request.recordKey())
                    .as("RecordKey는 필수 필드입니다: " + jsonFile)
                    .isNotNull()
                    .matches("^(SAMSUNG_USER_\\d{3}|APPLE_USER_\\d{3})$");
            
            assertThat(request.steps())
                    .as("Steps는 양수여야 합니다: " + jsonFile)
                    .isPositive();
            
            assertThat(request.calories())
                    .as("Calories는 양수여야 합니다: " + jsonFile)
                    .isGreaterThan(BigDecimal.ZERO);
            
            assertThat(request.distance())
                    .as("Distance는 양수여야 합니다: " + jsonFile)
                    .isGreaterThan(BigDecimal.ZERO);
            
            assertThat(request.collectedAt())
                    .as("CollectedAt은 필수 필드입니다: " + jsonFile)
                    .isNotNull()
                    .isAfter(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .isBefore(LocalDateTime.of(2024, 12, 31, 23, 59));
        }
    }

    @Test
    @DisplayName("JSON 파일별 활동 패턴 분석 테스트")
    void analyzeActivityPatterns_FromJsonFiles_Success() throws IOException {
        // given & when - JSON 파일별 활동 패턴 분석
        
        // INPUT_DATA1: 아침 고강도 운동 (조깅)
        SaveHealthDataRequest morningJogging = loadJsonFile("json/INPUT_DATA1.json");
        assertThat(morningJogging.collectedAt().getHour()).isLessThan(9); // 아침
        assertThat(morningJogging.steps()).isGreaterThan(8000); // 고강도
        assertThat(morningJogging.calories().doubleValue()).isGreaterThan(300);
        
        // INPUT_DATA2: 아침 중강도 활동 (출근길)
        SaveHealthDataRequest commuteWalk = loadJsonFile("json/INPUT_DATA2.json");
        assertThat(commuteWalk.collectedAt().getHour()).isLessThan(10); // 아침
        assertThat(commuteWalk.steps()).isBetween(3000, 5000); // 중강도
        assertThat(commuteWalk.calories().doubleValue()).isBetween(100.0, 200.0);
        
        // INPUT_DATA3: 점심시간 저강도 활동 (산책)
        SaveHealthDataRequest lunchWalk = loadJsonFile("json/INPUT_DATA3.json");
        assertThat(lunchWalk.collectedAt().getHour()).isEqualTo(12); // 점심시간
        assertThat(lunchWalk.steps()).isLessThan(3000); // 저강도
        assertThat(lunchWalk.calories().doubleValue()).isLessThan(100);
        
        // INPUT_DATA4: 저녁 고강도 운동 (헬스장)
        SaveHealthDataRequest eveningWorkout = loadJsonFile("json/INPUT_DATA4.json");
        assertThat(eveningWorkout.collectedAt().getHour()).isGreaterThan(18); // 저녁
        assertThat(eveningWorkout.steps()).isGreaterThan(10000); // 고강도
        assertThat(eveningWorkout.calories().doubleValue()).isGreaterThan(700);
    }

    private SaveHealthDataRequest loadJsonFile(String filePath) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("JSON 파일을 찾을 수 없습니다: " + filePath);
            }
            return objectMapper.readValue(inputStream, SaveHealthDataRequest.class);
        }
    }
}