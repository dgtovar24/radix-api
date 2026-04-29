package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalPatients;
    private long totalDoctors;
    private long activeTreatments;
    private long pendingAlerts;
    private long totalSmartwatches;
    private long activeIsotopes;
}