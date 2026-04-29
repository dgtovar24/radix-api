package com.project.radix.Service;

import com.project.radix.DTO.AlertResponse;
import com.project.radix.Model.DoctorAlert;
import com.project.radix.Model.Patient;
import com.project.radix.Model.Treatment;
import com.project.radix.Repository.AlertRepository;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final PatientRepository patientRepository;
    private final TreatmentRepository treatmentRepository;
    private final WebSocketNotificationService webSocketService;

    public void createRadiationAlert(Integer patientId, Integer treatmentId,
            Double currentRadiation, Double threshold) {
        Patient patient = patientRepository.findById(patientId).orElse(null);
        String patientName = patient != null ? patient.getFullName() : "Unknown";

        DoctorAlert alert = new DoctorAlert();
        alert.setFkPatientId(patientId);
        alert.setFkTreatmentId(treatmentId);
        alert.setAlertType("RADIATION_HIGH");
        alert.setMessage(String.format(
            "Radiation level (%.4f mCi) exceeded safety threshold (%.4f mCi)",
            currentRadiation, threshold));
        alert.setIsResolved(false);
        alert.setCreatedAt(LocalDateTime.now());
        alertRepository.save(alert);

        webSocketService.broadcastAlert(AlertResponse.builder()
            .id(alert.getId())
            .patientId(patientId)
            .patientName(patientName)
            .treatmentId(treatmentId)
            .alertType("RADIATION_HIGH")
            .message(alert.getMessage())
            .isResolved(false)
            .createdAt(alert.getCreatedAt())
            .build());
    }

    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<AlertResponse> getPendingAlerts() {
        return alertRepository.findByIsResolvedFalse().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<AlertResponse> getAlertsByPatient(Integer patientId) {
        return alertRepository.findByFkPatientId(patientId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public Optional<AlertResponse> resolveAlert(Integer id) {
        return alertRepository.findById(id)
            .map(alert -> {
                alert.setIsResolved(true);
                alertRepository.save(alert);
                return toResponse(alert);
            });
    }

    private AlertResponse toResponse(DoctorAlert alert) {
        Patient patient = patientRepository.findById(alert.getFkPatientId()).orElse(null);
        return AlertResponse.builder()
            .id(alert.getId())
            .patientId(alert.getFkPatientId())
            .patientName(patient != null ? patient.getFullName() : "Unknown")
            .treatmentId(alert.getFkTreatmentId())
            .alertType(alert.getAlertType())
            .message(alert.getMessage())
            .isResolved(alert.getIsResolved())
            .createdAt(alert.getCreatedAt())
            .build();
    }
}