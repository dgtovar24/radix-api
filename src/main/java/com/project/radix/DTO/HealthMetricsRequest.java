package com.project.radix.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HealthMetricsRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    private Integer fkTreatmentId;
    private Integer bpm;
    private Integer steps;
    private Double distance;
    private Double currentRadiation;
}
