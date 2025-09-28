package com.health.app.util;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;

@DisplayName("JSON 테스트 데이터 로더 유틸리티 테스트")
class JsonTestDataLoaderTest {

    @Test
    @DisplayName("개별 JSON 파일 로드 테스트")
    void loadHealthDataRequest_IndividualFiles_Success() {
        // when & then - 각 JSON 파일 로드 및 검증
        
        // INPUT_DATA1.json
        SaveHealthDataRequest data1 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA1.json");
        assertThat(data1.recordKey()).isEqualTo("SAMSUNG_USER_001");
        assertThat(data1.steps()).isEqualTo(8250);
        assertThat(data1.calories().doubleValue()).isEqualTo(387.5);
        assertThat(data1.distance().doubleValue()).isEqualTo(5.2);
        assertThat(data1.collectedAt()).isEqualTo(LocalDateTime.of(2024, 1, 15, 7, 30));

        // INPUT_DATA2.json
        SaveHealthDataRequest data2 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA2.json");
        assertThat(data2.recordKey()).isEqualTo("APPLE_USER_002");
        assertThat(data2.steps()).isEqualTo(3420);
        assertThat(data2.calories().doubleValue()).isEqualTo(158.3);

        // INPUT_DATA3.json
        SaveHealthDataRequest data3 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA3.json");
        assertThat(data3.recordKey()).isEqualTo("SAMSUNG_USER_001");
        assertThat(data3.steps()).isEqualTo(2100);
        assertThat(data3.collectedAt().getHour()).isEqualTo(12); // 점심시간

        // INPUT_DATA4.json
        SaveHealthDataRequest data4 = JsonTestDataLoader.loadHealthDataRequest("json/INPUT_DATA4.json");
        assertThat(data4.recordKey()).isEqualTo("APPLE_USER_002");
        assertThat(data4.steps()).isEqualTo(12500);
        assertThat(data4.collectedAt().getHour()).isEqualTo(19); // 저녁
    }

    @Test
    @DisplayName("모든 INPUT_DATA 파일 로드 테스트")
    void loadAllInputData_Success() {
        // when
        List<SaveHealthDataRequest> allData = JsonTestDataLoader.loadAllInputData();

        // then
        assertThat(allData).hasSize(4);
        assertThat(allData.stream().map(SaveHealthDataRequest::recordKey))
                .containsExactlyInAnyOrder(
                        "SAMSUNG_USER_001", "APPLE_USER_002", 
                        "SAMSUNG_USER_001", "APPLE_USER_002"
                );
    }

    @Test
    @DisplayName("삼성헬스 데이터만 로드 테스트")
    void loadSamsungHealthData_Success() {
        // when
        List<SaveHealthDataRequest> samsungData = JsonTestDataLoader.loadSamsungHealthData();

        // then
        assertThat(samsungData).hasSize(2);
        assertThat(samsungData).allMatch(data -> data.recordKey().equals("SAMSUNG_USER_001"));

        // 걸음 수 검증 (INPUT_DATA1: 8250, INPUT_DATA3: 2100)
        assertThat(samsungData.stream().mapToInt(SaveHealthDataRequest::steps).sum())
                .isEqualTo(8250 + 2100);
    }

    @Test
    @DisplayName("애플건강 데이터만 로드 테스트")
    void loadAppleHealthData_Success() {
        // when
        List<SaveHealthDataRequest> appleData = JsonTestDataLoader.loadAppleHealthData();

        // then
        assertThat(appleData).hasSize(2);
        assertThat(appleData).allMatch(data -> data.recordKey().equals("APPLE_USER_002"));

        // 걸음 수 검증 (INPUT_DATA2: 3420, INPUT_DATA4: 12500)
        assertThat(appleData.stream().mapToInt(SaveHealthDataRequest::steps).sum())
                .isEqualTo(3420 + 12500);
    }

    @Test
    @DisplayName("특정 사용자 데이터 로드 테스트")
    void loadDataByUser_Success() {
        // when
        List<SaveHealthDataRequest> samsungUserData = JsonTestDataLoader.loadDataByUser("SAMSUNG_USER_001");
        List<SaveHealthDataRequest> appleUserData = JsonTestDataLoader.loadDataByUser("APPLE_USER_002");

        // then
        assertThat(samsungUserData).hasSize(2);
        assertThat(appleUserData).hasSize(2);

        assertThat(samsungUserData).allMatch(data -> data.recordKey().equals("SAMSUNG_USER_001"));
        assertThat(appleUserData).allMatch(data -> data.recordKey().equals("APPLE_USER_002"));
    }

    @Test
    @DisplayName("시간대별 데이터 로드 테스트")
    void loadDataByTimeRange_Success() {
        // when - 아침 시간대 (6-10시)
        List<SaveHealthDataRequest> morningData = JsonTestDataLoader.loadDataByTimeRange(6, 10);
        
        // 점심 시간대 (12-14시)
        List<SaveHealthDataRequest> lunchData = JsonTestDataLoader.loadDataByTimeRange(12, 14);
        
        // 저녁 시간대 (18-22시)
        List<SaveHealthDataRequest> eveningData = JsonTestDataLoader.loadDataByTimeRange(18, 22);

        // then
        assertThat(morningData).hasSize(2); // INPUT_DATA1(7:30), INPUT_DATA2(8:45)
        assertThat(lunchData).hasSize(1);   // INPUT_DATA3(12:30)
        assertThat(eveningData).hasSize(1); // INPUT_DATA4(19:15)

        // 시간 범위 검증
        assertThat(morningData).allMatch(data -> {
            int hour = data.collectedAt().getHour();
            return hour >= 6 && hour <= 10;
        });

        assertThat(lunchData).allMatch(data -> {
            int hour = data.collectedAt().getHour();
            return hour >= 12 && hour <= 14;
        });

        assertThat(eveningData).allMatch(data -> {
            int hour = data.collectedAt().getHour();
            return hour >= 18 && hour <= 22;
        });
    }

    @Test
    @DisplayName("존재하지 않는 JSON 파일 로드 시 예외 처리 테스트")
    void loadHealthDataRequest_NonExistentFile_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> JsonTestDataLoader.loadHealthDataRequest("json/NON_EXISTENT.json"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("JSON 파일을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("JSON 데이터 일관성 검증 테스트")
    void validateJsonDataConsistency_Success() {
        // when
        List<SaveHealthDataRequest> allData = JsonTestDataLoader.loadAllInputData();

        // then - 데이터 일관성 검증
        for (SaveHealthDataRequest data : allData) {
            // 필수 필드 존재 검증
            assertThat(data.recordKey()).isNotNull().isNotEmpty();
            assertThat(data.steps()).isPositive();
            assertThat(data.calories()).isNotNull().isPositive();
            assertThat(data.distance()).isNotNull().isPositive();
            assertThat(data.collectedAt()).isNotNull();

            // 날짜 일관성 검증 (모든 데이터가 2024-01-15)
            assertThat(data.collectedAt().toLocalDate().toString()).isEqualTo("2024-01-15");

            // RecordKey 패턴 검증
            assertThat(data.recordKey()).matches("^(SAMSUNG_USER_\\d{3}|APPLE_USER_\\d{3})$");
        }

        // 총 데이터 개수 검증
        assertThat(allData).hasSize(4);

        // 사용자별 데이터 개수 검증
        long samsungCount = allData.stream()
                .filter(data -> data.recordKey().startsWith("SAMSUNG_USER"))
                .count();
        long appleCount = allData.stream()
                .filter(data -> data.recordKey().startsWith("APPLE_USER"))
                .count();

        assertThat(samsungCount).isEqualTo(2);
        assertThat(appleCount).isEqualTo(2);
    }

    @Test
    @DisplayName("활동 강도별 데이터 분류 테스트")
    void classifyActivityIntensity_Success() {
        // when
        List<SaveHealthDataRequest> allData = JsonTestDataLoader.loadAllInputData();

        // then - 활동 강도별 분류
        List<SaveHealthDataRequest> highIntensity = allData.stream()
                .filter(data -> data.calories().doubleValue() >= 700) // 고강도
                .toList();

        List<SaveHealthDataRequest> mediumIntensity = allData.stream()
                .filter(data -> data.calories().doubleValue() >= 200 && data.calories().doubleValue() < 700) // 중강도
                .toList();

        List<SaveHealthDataRequest> lowIntensity = allData.stream()
                .filter(data -> data.calories().doubleValue() < 200) // 저강도
                .toList();

        assertThat(highIntensity).hasSize(1);   // INPUT_DATA4 (742.8 kcal)
        assertThat(mediumIntensity).hasSize(1); // INPUT_DATA1 (387.5 kcal)
        assertThat(lowIntensity).hasSize(2);    // INPUT_DATA2 (158.3), INPUT_DATA3 (95.7)

        // 고강도 운동 검증 (저녁 시간, 높은 걸음 수)
        SaveHealthDataRequest highIntensityWorkout = highIntensity.get(0);
        assertThat(highIntensityWorkout.collectedAt().getHour()).isGreaterThan(18);
        assertThat(highIntensityWorkout.steps()).isGreaterThan(10000);
    }
}