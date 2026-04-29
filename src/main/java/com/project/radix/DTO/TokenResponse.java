package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String scope;
    private Integer patientId;
    private String patientName;
    private String familyAccessCode;
}