package com.health.app.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.health.app.application.port.in.SaveHealthDataCommand;
import com.health.app.application.port.in.SaveHealthDataUseCase;
import com.health.app.application.port.out.SaveHealthDataPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.health.Calories;
import com.health.app.domain.health.Distance;
import com.health.app.domain.health.HealthData;
import com.health.app.domain.health.Steps;

@Service
@Transactional
public class HealthDataService implements SaveHealthDataUseCase {

    private final SaveHealthDataPort saveHealthDataPort;

    public HealthDataService(SaveHealthDataPort saveHealthDataPort) {
        this.saveHealthDataPort = saveHealthDataPort;
    }

    @Override
    public HealthData saveHealthData(SaveHealthDataCommand command) {
        RecordKey recordKey = new RecordKey(command.recordKey());
        Steps steps = new Steps(command.steps());
        Calories calories = new Calories(command.calories());
        Distance distance = new Distance(command.distance());

        HealthData healthData;
        if (command.collectedAt() != null) {
            healthData =
                    new HealthData(recordKey, steps, calories, distance, command.collectedAt());
        } else {
            healthData = new HealthData(recordKey, steps, calories, distance);
        }

        return saveHealthDataPort.saveHealthData(healthData);
    }
}
