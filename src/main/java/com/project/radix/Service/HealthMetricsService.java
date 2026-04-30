package com.project.radix.Service;

import com.project.radix.DTO.HealthLogResponse;
import com.project.radix.DTO.HealthMetricsRequest;
import com.project.radix.DTO.HealthMetricsResponse;
import com.project.radix.DTO.RadiationLogResponse;
import com.project.radix.Model.HealthMetrics;
import com.project.radix.Repository.HealthLogRepository;
import com.project.radix.Repository.HealthMetricsRepository;
import com.project.radix.Repository.RadiationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthMetricsService {

    private final HealthMetricsRepository healthMetricsRepository;
    private final HealthLogRepository healthLogRepository;
    private final RadiationLogRepository radiationLogRepository;

    public List<HealthMetricsResponse> getByPatient(Integer patientId, Integer days) {
        var metrics = healthMetricsRepository.findByFkPatientIdOrderByRecordedAtDesc(patientId);
        if (days != null && days > 0) {
            var cutoff = LocalDateTime.now().minusDays(days);
            metrics = metrics.stream().filter(m -> m.getRecordedAt().isAfter(cutoff)).toList();
        }
        return metrics.stream().map(this::toResponse).toList();
    }

    public HealthMetricsResponse getLatest(Integer patientId) {
        return healthMetricsRepository.findFirstByFkPatientIdOrderByRecordedAtDesc(patientId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("No metrics found"));
    }

    public List<HealthMetricsResponse> getByTreatment(Integer treatmentId) {
        return healthMetricsRepository.findByFkTreatmentId(treatmentId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public HealthMetricsResponse create(HealthMetricsRequest request) {
        HealthMetrics m = new HealthMetrics();
        m.setFkPatientId(request.getFkPatientId());
        m.setFkTreatmentId(request.getFkTreatmentId());
        m.setBpm(request.getBpm());
        m.setSteps(request.getSteps());
        m.setDistance(request.getDistance());
        m.setCurrentRadiation(request.getCurrentRadiation());
        m.setRecordedAt(LocalDateTime.now());
        m = healthMetricsRepository.save(m);
        return toResponse(m);
    }

    public List<HealthLogResponse> getHealthLogs(Integer patientId, Integer days) {
        var logs = healthLogRepository.findByFkPatientIdOrderByTimestampDesc(patientId);
        if (days != null && days > 0) {
            var cutoff = LocalDateTime.now().minusDays(days);
            logs = logs.stream().filter(l -> l.getTimestamp().isAfter(cutoff)).toList();
        }
        return logs.stream().map(l -> HealthLogResponse.builder()
                .id(l.getId())
                .patientId(l.getFkPatientId())
                .bpm(l.getBpm())
                .steps(l.getSteps())
                .distance(l.getDistance())
                .timestamp(l.getTimestamp())
                .build()).toList();
    }

    public List<RadiationLogResponse> getRadiationByPatient(Integer patientId, Integer days) {
        var logs = radiationLogRepository.findByFkPatientIdOrderByTimestampDesc(patientId);
        if (days != null && days > 0) {
            var cutoff = LocalDateTime.now().minusDays(days);
            logs = logs.stream().filter(l -> l.getTimestamp().isAfter(cutoff)).toList();
        }
        return logs.stream().map(l -> RadiationLogResponse.builder()
                .id(l.getId())
                .patientId(l.getFkPatientId())
                .treatmentId(l.getFkTreatmentId())
                .radiationLevel(l.getRadiationLevel())
                .timestamp(l.getTimestamp())
                .build()).toList();
    }

    public List<RadiationLogResponse> getRadiationByTreatment(Integer treatmentId) {
        return radiationLogRepository.findByFkTreatmentIdOrderByTimestampDesc(treatmentId).stream()
                .map(l -> RadiationLogResponse.builder()
                        .id(l.getId())
                        .patientId(l.getFkPatientId())
                        .treatmentId(l.getFkTreatmentId())
                        .radiationLevel(l.getRadiationLevel())
                        .timestamp(l.getTimestamp())
                        .build()).toList();
    }

    private HealthMetricsResponse toResponse(HealthMetrics m) {
        return HealthMetricsResponse.builder()
                .id(m.getId())
                .patientId(m.getFkPatientId())
                .treatmentId(m.getFkTreatmentId())
                .bpm(m.getBpm())
                .steps(m.getSteps())
                .distance(m.getDistance())
                .currentRadiation(m.getCurrentRadiation())
                .recordedAt(m.getRecordedAt())
                .build();
    }
}
