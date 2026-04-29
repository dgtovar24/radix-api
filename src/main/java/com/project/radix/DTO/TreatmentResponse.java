package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TreatmentResponse {
    private Integer id;
    private String patientName;
    private Integer patientId;
    private String doctorName;
    private Integer doctorId;
    private String isotopeName;
    private Integer isotopeId;
    private Integer room;
    private Double initialDose;
    private Double safetyThreshold;
    private Integer isolationDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private Double currentRadiation;
}