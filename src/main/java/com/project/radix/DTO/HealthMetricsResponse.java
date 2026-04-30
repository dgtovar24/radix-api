package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class HealthMetricsResponse {
    private Integer id;
    private Integer patientId;
    private Integer treatmentId;
    private Integer bpm;
    private Integer steps;
    private Double distance;
    private Double currentRadiation;
    private LocalDateTime recordedAt;
}
