package com.health.app.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.health.app.application.port.in.SaveHealthDataCommand;
import com.health.app.application.port.out.SaveHealthDataPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.HealthData;
import com.health.app.domain.health.Steps;
import com.health.app.fixture.HealthAppDataFixture;

@DisplayName("헬스 앱 데이터 서비스 단위 테스트")
class HealthAppDataServiceTest {

    @Mock
    private SaveHealthDataPort saveHealthDataPort;

    private HealthDataService healthDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        healthDataService = new HealthDataService(saveHealthDataPort);
    }

    @Test
    @DisplayName("삼성헬스 데이터 저장 - INPUT_DATA1")
    void saveHealthData_SamsungHealthData_Success() {
        // given
        var inputData1 = HealthAppDataFixture.INPUT_DATA1;
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                inputData1.recordKey(),
                inputData1.steps(),
                inputData1.calories(),
                inputData1.distance(),
                inputData1.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("SAMSUNG_USER_001"),
                new Steps(8250),
                new Calories(387.5),
                new Distance(5.2),
                inputData1.collectedAt()
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then
        assertThat(result.getRecordKey().getValue()).isEqualTo("SAMSUNG_USER_001");
        assertThat(result.getSteps().getValue()).isEqualTo(8250);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(387.5);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(5.2);
        assertThat(result.getCollectedAt()).isEqualTo(inputData1.collectedAt());

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("애플건강 데이터 저장 - INPUT_DATA2")
    void saveHealthData_AppleHealthData_Success() {
        // given
        var inputData2 = HealthAppDataFixture.INPUT_DATA2;
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                inputData2.recordKey(),
                inputData2.steps(),
                inputData2.calories(),
                inputData2.distance(),
                inputData2.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("APPLE_USER_002"),
                new Steps(3420),
                new Calories(158.3),
                new Distance(2.1),
                inputData2.collectedAt()
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then
        assertThat(result.getRecordKey().getValue()).isEqualTo("APPLE_USER_002");
        assertThat(result.getSteps().getValue()).isEqualTo(3420);
        assertThat(result.getCalories().getDoubleValue()).isEqualTo(158.3);
        assertThat(result.getDistance().getDoubleValue()).isEqualTo(2.1);

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("고강도 운동 데이터 저장 - 주말 하이킹")
    void saveHealthData_HighIntensityWorkout_Success() {
        // given
        var hikingData = HealthAppDataFixture.WEEKEND_HIKING_DATA;
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                hikingData.recordKey(),
                hikingData.steps(),
                hikingData.calories(),
                hikingData.distance(),
                hikingData.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("SAMSUNG_USER_001"),
                new Steps(15680),
                new Calories(892.4),
                new Distance(11.2),
                hikingData.collectedAt()
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 고강도 운동 데이터 검증
        assertThat(result.getSteps().getValue()).isGreaterThan(15000);
        assertThat(result.getCalories().getDoubleValue()).isGreaterThan(800);
        assertThat(result.getDistance().getDoubleValue()).isGreaterThan(10);

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("저강도 활동 데이터 저장 - 점심시간 산책")
    void saveHealthData_LowIntensityActivity_Success() {
        // given
        var lunchWalkData = HealthAppDataFixture.INPUT_DATA3;
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                lunchWalkData.recordKey(),
                lunchWalkData.steps(),
                lunchWalkData.calories(),
                lunchWalkData.distance(),
                lunchWalkData.collectedAt()
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("SAMSUNG_USER_001"),
                new Steps(2100),
                new Calories(95.7),
                new Distance(1.3),
                lunchWalkData.collectedAt()
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then - 저강도 활동 데이터 검증
        assertThat(result.getSteps().getValue()).isLessThan(5000);
        assertThat(result.getCalories().getDoubleValue()).isLessThan(200);
        assertThat(result.getDistance().getDoubleValue()).isLessThan(2);

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("수집 시간이 없는 데이터 저장 - 현재 시간으로 저장")
    void saveHealthData_NoCollectedTime_UseCurrentTime() {
        // given - collectedAt이 null인 경우
        SaveHealthDataCommand command = new SaveHealthDataCommand(
                "TEST_USER",
                5000,
                new java.math.BigDecimal("250.0"),
                new java.math.BigDecimal("3.5"),
                null // collectedAt이 null
        );

        HealthData expectedHealthData = new HealthData(
                new RecordKey("TEST_USER"),
                new Steps(5000),
                new Calories(250.0),
                new Distance(3.5)
        );

        when(saveHealthDataPort.saveHealthData(any(HealthData.class)))
                .thenReturn(expectedHealthData);

        // when
        HealthData result = healthDataService.saveHealthData(command);

        // then
        assertThat(result.getCollectedAt()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();

        verify(saveHealthDataPort).saveHealthData(any(HealthData.class));
    }

    @Test
    @DisplayName("Command 생성 시 잘못된 RecordKey 검증")
    void createCommand_InvalidRecordKey_ThrowsException() {
        // when & then - Command 생성 시점에서 검증
        assertThatThrownBy(() -> new SaveHealthDataCommand(
                "", // 빈 RecordKey
                5000,
                new java.math.BigDecimal("250.0"),
                new java.math.BigDecimal("3.5"),
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("RecordKey cannot be null or empty");
    }

    @Test
    @DisplayName("Command 생성 시 음수 걸음 수 검증")
    void createCommand_NegativeSteps_ThrowsException() {
        // when & then - Command 생성 시점에서 검증
        assertThatThrownBy(() -> new SaveHealthDataCommand(
                "TEST_USER",
                -1000, // 음수 걸음 수
                new java.math.BigDecimal("250.0"),
                new java.math.BigDecimal("3.5"),
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Steps cannot be negative");
    }
}