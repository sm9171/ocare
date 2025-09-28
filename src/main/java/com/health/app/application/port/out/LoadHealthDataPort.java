package com.health.app.application.port.out;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.HealthData;

public interface LoadHealthDataPort {

    Optional<HealthData> loadHealthDataById(Long id);

    List<HealthData> loadHealthDataByRecordKey(RecordKey recordKey);

    List<HealthData> loadHealthDataByRecordKeyAndDateRange(
            RecordKey recordKey, LocalDateTime startDate, LocalDateTime endDate);

    List<HealthData> loadHealthDataByRecordKeyAndDate(RecordKey recordKey, LocalDate date);
}
