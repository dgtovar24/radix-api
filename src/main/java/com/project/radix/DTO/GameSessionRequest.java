package com.project.radix.DTO;

import lombok.Data;

@Data
public class GameSessionRequest {
    private Integer fkPatientId;
    private Integer score;
    private Integer levelReached;
}
