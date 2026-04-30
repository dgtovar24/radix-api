package com.project.radix.DTO;

import lombok.Data;

@Data
public class HealthMetricsRequest {
    private Integer fkPatientId;
    private Integer fkTreatmentId;
    private Integer bpm;
    private Integer steps;
    private Double distance;
    private Double currentRadiation;
}
