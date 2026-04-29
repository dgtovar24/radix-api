package com.project.radix.Controller;

import com.project.radix.DTO.DashboardStatsResponse;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.TreatmentRepository;
import com.project.radix.Repository.UserRepository;
import com.project.radix.Repository.AlertRepository;
import com.project.radix.Repository.SmartwatchRepository;
import com.project.radix.Repository.IsotopeCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final TreatmentRepository treatmentRepository;
    private final AlertRepository alertRepository;
    private final SmartwatchRepository smartwatchRepository;
    private final IsotopeCatalogRepository isotopeRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getStats() {
        long totalPatients = patientRepository.count();
        long totalDoctors = userRepository.findByRole("DOCTOR").size();
        long activeTreatments = treatmentRepository.findByIsActiveTrue().size();
        long pendingAlerts = alertRepository.findByIsResolvedFalse().size();
        long totalSmartwatches = smartwatchRepository.count();
        long activeIsotopes = isotopeRepository.count();

        return ResponseEntity.ok(DashboardStatsResponse.builder()
            .totalPatients(totalPatients)
            .totalDoctors(totalDoctors)
            .activeTreatments(activeTreatments)
            .pendingAlerts(pendingAlerts)
            .totalSmartwatches(totalSmartwatches)
            .activeIsotopes(activeIsotopes)
            .build());
    }
}