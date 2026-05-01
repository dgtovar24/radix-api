package com.project.radix.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SmartwatchRequest {
    @NotNull(message = "Patient ID is required")
    private Integer fkPatientId;

    @NotBlank(message = "IMEI is required")
    private String imei;

    private String macAddress;
    private String model;
}
