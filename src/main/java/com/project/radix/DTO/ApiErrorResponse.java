package com.project.radix.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private String code;
    private String message;
    private Object details;

    public ApiErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}