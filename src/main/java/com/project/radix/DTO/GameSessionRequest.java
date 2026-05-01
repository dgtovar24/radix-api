package com.project.radix.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameSessionRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    @NotNull(message = "Score is required")
    private Integer score;

    private Integer levelReached;
}
