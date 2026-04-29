package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AlertResponse {
    private Integer id;
    private String patientName;
    private Integer patientId;
    private Integer treatmentId;
    private String alertType;
    private String message;
    private Boolean isResolved;
    private LocalDateTime createdAt;
}