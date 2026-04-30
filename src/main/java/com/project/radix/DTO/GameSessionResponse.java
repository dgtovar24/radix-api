package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GameSessionResponse {
    private Integer id;
    private Integer patientId;
    private Integer score;
    private Integer levelReached;
    private LocalDateTime playedAt;
}
