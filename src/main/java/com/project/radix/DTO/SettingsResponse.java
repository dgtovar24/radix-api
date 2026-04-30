package com.project.radix.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SettingsResponse {
    private Integer id;
    private Integer patientId;
    private String unitPreference;
    private String theme;
    private Boolean notificationsEnabled;
    private LocalDateTime updatedAt;
}
