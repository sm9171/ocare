package com.health.app.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveHealthDataRequest(
        @NotBlank(message = "RecordKey cannot be blank") String recordKey,
        @NotNull(message = "Steps cannot be null") @Min(value = 0, message = "Steps cannot be negative")
                Integer steps,
        @NotNull(message = "Calories cannot be null") @DecimalMin(value = "0.0", message = "Calories cannot be negative")
                BigDecimal calories,
        @NotNull(message = "Distance cannot be null") @DecimalMin(value = "0.0", message = "Distance cannot be negative")
                BigDecimal distance,
        LocalDateTime collectedAt) {}
