package com.project.radix.DTO;

import lombok.Data;

@Data
public class TokenRequest {
    private String grantType;
    private String clientId;
    private String clientSecret;
    private String scope;
}