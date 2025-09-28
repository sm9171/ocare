package com.health.app.application.port.in;

import com.health.app.domain.health.HealthData;

public interface SaveHealthDataUseCase {

    HealthData saveHealthData(SaveHealthDataCommand command);
}
