package com.health.app.domain.health;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.health.app.domain.common.RecordKey;

public interface HealthDataRepository {

    HealthData save(HealthData healthData);

    Optional<HealthData> findById(Long id);

    List<HealthData> findByRecordKey(RecordKey recordKey);

    List<HealthData> findByRecordKeyAndDateRange(
            RecordKey recordKey, LocalDateTime startDate, LocalDateTime endDate);

    List<HealthData> findByRecordKeyAndDate(RecordKey recordKey, LocalDate date);

    void delete(HealthData healthData);

    void deleteByRecordKey(RecordKey recordKey);
}
