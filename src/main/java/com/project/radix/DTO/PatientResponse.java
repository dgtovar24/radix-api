package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PatientResponse {
    private Integer id;
    private String fullName;
    private String phone;
    private String address;
    private Boolean isActive;
    private String familyAccessCode;
    private Integer fkUserId;
    private Integer fkDoctorId;
    private LocalDateTime createdAt;
}
