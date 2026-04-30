package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class HealthLogResponse {
    private Integer id;
    private Integer patientId;
    private Integer bpm;
    private Integer steps;
    private Double distance;
    private LocalDateTime timestamp;
}
