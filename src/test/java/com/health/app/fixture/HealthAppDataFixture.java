package com.health.app.fixture;

import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 삼성헬스, 애플건강 등에서 수집되는 실제 데이터를 시뮬레이션하는 테스트 픽스처
 */
public class HealthAppDataFixture {

    // 삼성헬스에서 수집된 아침 운동 데이터
    public static final SaveHealthDataRequest INPUT_DATA1 = new SaveHealthDataRequest(
            "SAMSUNG_USER_001", 
            8250,  // 아침 조깅으로 걸은 걸음
            BigDecimal.valueOf(387.5), // 소모된 칼로리
            BigDecimal.valueOf(5.2),   // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 15, 7, 30) // 아침 7시 30분
    );

    // 애플건강에서 수집된 출근길 데이터
    public static final SaveHealthDataRequest INPUT_DATA2 = new SaveHealthDataRequest(
            "APPLE_USER_002",
            3420,  // 지하철역까지 걸어간 걸음
            BigDecimal.valueOf(158.3), // 소모된 칼로리
            BigDecimal.valueOf(2.1),   // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 15, 8, 45) // 아침 8시 45분
    );

    // 삼성헬스에서 수집된 점심시간 산책 데이터
    public static final SaveHealthDataRequest INPUT_DATA3 = new SaveHealthDataRequest(
            "SAMSUNG_USER_001", // 같은 사용자의 추가 데이터
            2100,  // 점심시간 산책
            BigDecimal.valueOf(95.7),  // 소모된 칼로리
            BigDecimal.valueOf(1.3),   // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 15, 12, 30) // 점심 12시 30분
    );

    // 애플건강에서 수집된 저녁 운동 데이터
    public static final SaveHealthDataRequest INPUT_DATA4 = new SaveHealthDataRequest(
            "APPLE_USER_002", // 같은 사용자의 추가 데이터
            12500, // 헬스장에서 운동 + 집까지 걸어온 걸음
            BigDecimal.valueOf(742.8), // 소모된 칼로리
            BigDecimal.valueOf(8.7),   // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 15, 19, 15) // 저녁 7시 15분
    );

    // 주말 하이킹 데이터 (삼성헬스)
    public static final SaveHealthDataRequest WEEKEND_HIKING_DATA = new SaveHealthDataRequest(
            "SAMSUNG_USER_001",
            15680, // 하이킹으로 걸은 걸음
            BigDecimal.valueOf(892.4), // 소모된 칼로리
            BigDecimal.valueOf(11.2),  // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 20, 14, 30) // 토요일 오후 2시 30분
    );

    // 새벽 러닝 데이터 (애플건강)
    public static final SaveHealthDataRequest EARLY_RUNNING_DATA = new SaveHealthDataRequest(
            "APPLE_USER_002",
            6780,  // 새벽 러닝
            BigDecimal.valueOf(456.2), // 소모된 칼로리
            BigDecimal.valueOf(4.8),   // 이동 거리 (km)
            LocalDateTime.of(2024, 1, 21, 6, 0) // 일요일 새벽 6시
    );

    // 모든 테스트 데이터 목록
    public static List<SaveHealthDataRequest> getAllTestData() {
        return List.of(
                INPUT_DATA1,
                INPUT_DATA2,
                INPUT_DATA3,
                INPUT_DATA4,
                WEEKEND_HIKING_DATA,
                EARLY_RUNNING_DATA
        );
    }

    // 같은 날짜의 데이터 목록 (2024-01-15)
    public static List<SaveHealthDataRequest> getSameDayData() {
        return List.of(INPUT_DATA1, INPUT_DATA2, INPUT_DATA3, INPUT_DATA4);
    }

    // 삼성헬스 데이터만
    public static List<SaveHealthDataRequest> getSamsungHealthData() {
        return List.of(INPUT_DATA1, INPUT_DATA3, WEEKEND_HIKING_DATA);
    }

    // 애플건강 데이터만
    public static List<SaveHealthDataRequest> getAppleHealthData() {
        return List.of(INPUT_DATA2, INPUT_DATA4, EARLY_RUNNING_DATA);
    }
}