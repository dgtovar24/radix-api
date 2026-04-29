package com.project.radix.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlertCreateRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    private Integer fkTreatmentId;

    @NotBlank(message = "Alert type is required")
    private String alertType;

    @NotBlank(message = "Message is required")
    private String message;
}