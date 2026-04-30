package com.project.radix.DTO;

import lombok.Data;

@Data
public class MessageRequest {
    private Integer fkPatientId;
    private String messageText;
}
