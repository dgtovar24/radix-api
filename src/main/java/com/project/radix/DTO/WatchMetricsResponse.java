package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class WatchMetricsResponse {
    private Integer id;
    private Integer patientId;
    private String patientName;
    private String imei;
    private Integer bpm;
    private Integer steps;
    private Double distance;
    private Double currentRadiation;
    private LocalDateTime recordedAt;
}