package com.project.radix.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WatchIngestRequest {
    @NotBlank(message = "IMEI is required")
    private String imei;

    @NotBlank(message = "Family access code is required")
    private String familyAccessCode;

    private Integer bpm;
    private Integer steps;
    private Double distance;

    @NotNull(message = "Current radiation is required")
    private Double currentRadiation;

    private LocalDateTime recordedAt;
}