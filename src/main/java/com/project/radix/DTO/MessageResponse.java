package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MessageResponse {
    private Integer id;
    private Integer patientId;
    private String messageText;
    private Boolean isRead;
    private LocalDateTime sentAt;
}
