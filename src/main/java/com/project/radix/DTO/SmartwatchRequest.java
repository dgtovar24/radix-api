package com.project.radix.DTO;

import lombok.Data;

@Data
public class SmartwatchRequest {
    private Integer fkPatientId;
    private String imei;
    private String macAddress;
    private String model;
}
