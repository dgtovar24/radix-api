package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SmartwatchResponse {
    private Integer id;
    private String imei;
    private String macAddress;
    private String model;
    private Boolean isActive;
    private Integer patientId;
    private String patientName;
}
