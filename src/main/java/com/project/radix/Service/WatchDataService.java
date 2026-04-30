package com.project.radix.Service;

import com.project.radix.DTO.WatchIngestRequest;
import com.project.radix.DTO.WatchMetricsResponse;
import com.project.radix.Model.HealthMetrics;
import com.project.radix.Model.Patient;
import com.project.radix.Model.Smartwatch;
import com.project.radix.Repository.HealthMetricsRepository;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.SmartwatchRepository;
import com.project.radix.Repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchDataService {

    private final HealthMetricsRepository metricsRepository;
    private final PatientRepository patientRepository;
    private final SmartwatchRepository smartwatchRepository;
    private final TreatmentRepository treatmentRepository;
    private final AlertService alertService;

    @Transactional
    public void ingestData(WatchIngestRequest request) {
        Patient patient = patientRepository.findByFamilyAccessCode(request.getFamilyAccessCode())
            .orElseThrow(() -> new RuntimeException("Invalid family access code"));

        Smartwatch watch = smartwatchRepository.findByImei(request.getImei())
            .orElseThrow(() -> new RuntimeException("Smartwatch not registered"));

        if (!watch.getFkPatientId().equals(patient.getId())) {
            throw new RuntimeException("Smartwatch not linked to this patient");
        }

        HealthMetrics metrics = new HealthMetrics();
        metrics.setFkPatientId(patient.getId());
        metrics.setFkTreatmentId(null);
        metrics.setBpm(request.getBpm());
        metrics.setSteps(request.getSteps());
        metrics.setDistance(request.getDistance());
        metrics.setCurrentRadiation(request.getCurrentRadiation());
        metrics.setRecordedAt(request.getRecordedAt() != null ? request.getRecordedAt() : LocalDateTime.now());

        metricsRepository.save(metrics);

        var activeTreatments = treatmentRepository.findByFkPatientIdAndIsActiveTrue(patient.getId());
        if (!activeTreatments.isEmpty()) {
            var treatment = activeTreatments.get(0);
            if (request.getCurrentRadiation() > treatment.getSafetyThreshold()) {
                alertService.createRadiationAlert(
                    patient.getId(),
                    treatment.getId(),
                    request.getCurrentRadiation(),
                    treatment.getSafetyThreshold()
                );
            }
        }
    }

    public List<WatchMetricsResponse> getMetricsByImei(String imei) {
        Smartwatch watch = smartwatchRepository.findByImei(imei).orElse(null);
        if (watch == null) return List.of();

        return metricsRepository.findByFkPatientIdOrderByRecordedAtDesc(watch.getFkPatientId())
            .stream()
            .map(m -> WatchMetricsResponse.builder()
                .id(m.getId())
                .patientId(m.getFkPatientId())
                .imei(imei)
                .bpm(m.getBpm())
                .steps(m.getSteps())
                .distance(m.getDistance())
                .currentRadiation(m.getCurrentRadiation())
                .recordedAt(m.getRecordedAt())
                .build())
            .toList();
    }

    public Optional<WatchMetricsResponse> getLatestMetricsByPatient(Integer patientId) {
        return metricsRepository.findFirstByFkPatientIdOrderByRecordedAtDesc(patientId)
            .map(m -> {
                String imei = "";
                var watches = smartwatchRepository.findByFkPatientId(patientId);
                if (!watches.isEmpty()) imei = watches.get(0).getImei();

                return WatchMetricsResponse.builder()
                    .id(m.getId())
                    .patientId(m.getFkPatientId())
                    .imei(imei)
                    .bpm(m.getBpm())
                    .steps(m.getSteps())
                    .distance(m.getDistance())
                    .currentRadiation(m.getCurrentRadiation())
                    .recordedAt(m.getRecordedAt())
                    .build();
            });
    }
}