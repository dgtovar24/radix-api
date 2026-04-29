package com.project.radix.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TreatmentCreateRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    @NotNull(message = "Radioisotope ID is required")
    private Integer fkRadioisotopeId;

    private Integer fkSmartwatchId;

    @NotNull(message = "Room number is required")
    private Integer room;

    @NotNull(message = "Initial dose is required")
    private Double initialDose;
}