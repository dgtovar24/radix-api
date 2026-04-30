package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RadiationLogResponse {
    private Integer id;
    private Integer patientId;
    private Integer treatmentId;
    private Double radiationLevel;
    private LocalDateTime timestamp;
}
