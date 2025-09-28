package com.health.app.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.health.app.adapter.in.web.dto.HealthDataResponse;
import com.health.app.adapter.in.web.dto.SaveHealthDataRequest;
import com.health.app.application.port.in.SaveHealthDataCommand;
import com.health.app.application.port.in.SaveHealthDataUseCase;
import com.health.app.domain.health.HealthData;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/health-data")
public class HealthDataController {

    private final SaveHealthDataUseCase saveHealthDataUseCase;

    public HealthDataController(SaveHealthDataUseCase saveHealthDataUseCase) {
        this.saveHealthDataUseCase = saveHealthDataUseCase;
    }

    @PostMapping
    public ResponseEntity<HealthDataResponse> saveHealthData(
            @Valid @RequestBody SaveHealthDataRequest request) {
        SaveHealthDataCommand command =
                new SaveHealthDataCommand(
                        request.recordKey(),
                        request.steps(),
                        request.calories(),
                        request.distance(),
                        request.collectedAt());

        HealthData healthData = saveHealthDataUseCase.saveHealthData(command);
        HealthDataResponse response = HealthDataResponse.from(healthData);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
