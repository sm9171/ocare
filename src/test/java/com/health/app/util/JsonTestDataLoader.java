package com.health.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;

/**
 * JSON 테스트 데이터 로더 유틸리티
 */
public class JsonTestDataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * JSON 파일로부터 SaveHealthDataRequest 객체를 로드합니다.
     */
    public static SaveHealthDataRequest loadHealthDataRequest(String filePath) {
        try (InputStream inputStream = JsonTestDataLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("JSON 파일을 찾을 수 없습니다: " + filePath);
            }
            return objectMapper.readValue(inputStream, SaveHealthDataRequest.class);
        } catch (IOException e) {
            throw new RuntimeException("JSON 파일 로드 중 오류 발생: " + filePath, e);
        }
    }

    /**
     * 모든 INPUT_DATA JSON 파일을 로드합니다.
     */
    public static List<SaveHealthDataRequest> loadAllInputData() {
        return List.of(
                loadHealthDataRequest("json/INPUT_DATA1.json"),
                loadHealthDataRequest("json/INPUT_DATA2.json"),
                loadHealthDataRequest("json/INPUT_DATA3.json"),
                loadHealthDataRequest("json/INPUT_DATA4.json")
        );
    }

    /**
     * 삼성헬스 데이터만 로드합니다 (INPUT_DATA1, INPUT_DATA3).
     */
    public static List<SaveHealthDataRequest> loadSamsungHealthData() {
        return List.of(
                loadHealthDataRequest("json/INPUT_DATA1.json"),
                loadHealthDataRequest("json/INPUT_DATA3.json")
        );
    }

    /**
     * 애플건강 데이터만 로드합니다 (INPUT_DATA2, INPUT_DATA4).
     */
    public static List<SaveHealthDataRequest> loadAppleHealthData() {
        return List.of(
                loadHealthDataRequest("json/INPUT_DATA2.json"),
                loadHealthDataRequest("json/INPUT_DATA4.json")
        );
    }

    /**
     * 특정 사용자의 데이터만 로드합니다.
     */
    public static List<SaveHealthDataRequest> loadDataByUser(String recordKey) {
        return loadAllInputData().stream()
                .filter(data -> data.recordKey().equals(recordKey))
                .toList();
    }

    /**
     * 특정 시간대의 데이터만 로드합니다.
     */
    public static List<SaveHealthDataRequest> loadDataByTimeRange(int startHour, int endHour) {
        return loadAllInputData().stream()
                .filter(data -> {
                    int hour = data.collectedAt().getHour();
                    return hour >= startHour && hour <= endHour;
                })
                .toList();
    }
}