package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientResponse {
    private Integer id;
    private String fullName;
    private Boolean isActive;
}
