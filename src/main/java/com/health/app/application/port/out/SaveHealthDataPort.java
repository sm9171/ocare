package com.health.app.application.port.out;

import com.health.app.domain.health.HealthData;

public interface SaveHealthDataPort {

    HealthData saveHealthData(HealthData healthData);
}
