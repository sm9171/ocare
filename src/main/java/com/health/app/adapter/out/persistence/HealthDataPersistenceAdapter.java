package com.health.app.adapter.out.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.health.app.application.port.out.LoadHealthDataPort;
import com.health.app.application.port.out.SaveHealthDataPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.HealthData;

@Component
public class HealthDataPersistenceAdapter implements LoadHealthDataPort, SaveHealthDataPort {

    private final HealthDataJpaRepository healthDataJpaRepository;

    public HealthDataPersistenceAdapter(HealthDataJpaRepository healthDataJpaRepository) {
        this.healthDataJpaRepository = healthDataJpaRepository;
    }

    @Override
    public Optional<HealthData> loadHealthDataById(Long id) {
        return healthDataJpaRepository.findById(id).map(HealthDataJpaEntity::toDomain);
    }

    @Override
    public List<HealthData> loadHealthDataByRecordKey(RecordKey recordKey) {
        return healthDataJpaRepository.findByRecordKey(recordKey.getValue()).stream()
                .map(HealthDataJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<HealthData> loadHealthDataByRecordKeyAndDateRange(
            RecordKey recordKey, LocalDateTime startDate, LocalDateTime endDate) {
        return healthDataJpaRepository
                .findByRecordKeyAndCollectedAtBetween(recordKey.getValue(), startDate, endDate)
                .stream()
                .map(HealthDataJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<HealthData> loadHealthDataByRecordKeyAndDate(RecordKey recordKey, LocalDate date) {
        LocalDateTime dateTime = date.atStartOfDay();
        return healthDataJpaRepository
                .findByRecordKeyAndDate(recordKey.getValue(), dateTime)
                .stream()
                .map(HealthDataJpaEntity::toDomain)
                .toList();
    }

    @Override
    public HealthData saveHealthData(HealthData healthData) {
        HealthDataJpaEntity entity = HealthDataJpaEntity.fromDomain(healthData);
        HealthDataJpaEntity savedEntity = healthDataJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
}
