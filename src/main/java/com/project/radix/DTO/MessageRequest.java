package com.project.radix.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    @NotBlank(message = "Message text is required")
    private String messageText;
}
