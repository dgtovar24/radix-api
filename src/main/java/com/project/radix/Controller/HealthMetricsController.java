package com.project.radix.Controller;

import com.project.radix.DTO.HealthLogResponse;
import com.project.radix.DTO.HealthMetricsRequest;
import com.project.radix.DTO.HealthMetricsResponse;
import com.project.radix.DTO.RadiationLogResponse;
import com.project.radix.Service.HealthMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HealthMetricsController {

    private final HealthMetricsService service;

    @GetMapping("/api/health-metrics/patient/{patientId}")
    public ResponseEntity<List<HealthMetricsResponse>> getByPatient(
            @PathVariable Integer patientId,
            @RequestParam(required = false) Integer days) {
        return ResponseEntity.ok(service.getByPatient(patientId, days));
    }

    @GetMapping("/api/health-metrics/patient/{patientId}/latest")
    public ResponseEntity<?> getLatest(@PathVariable Integer patientId) {
        try {
            return ResponseEntity.ok(service.getLatest(patientId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/api/health-metrics/treatment/{treatmentId}")
    public ResponseEntity<List<HealthMetricsResponse>> getByTreatment(@PathVariable Integer treatmentId) {
        return ResponseEntity.ok(service.getByTreatment(treatmentId));
    }

    @PostMapping("/api/health-metrics")
    public ResponseEntity<HealthMetricsResponse> create(@RequestBody HealthMetricsRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @GetMapping("/api/health-logs/patient/{patientId}")
    public ResponseEntity<List<HealthLogResponse>> getHealthLogs(
            @PathVariable Integer patientId,
            @RequestParam(required = false) Integer days) {
        return ResponseEntity.ok(service.getHealthLogs(patientId, days));
    }

    @GetMapping("/api/radiation-logs/patient/{patientId}")
    public ResponseEntity<List<RadiationLogResponse>> getRadiationByPatient(
            @PathVariable Integer patientId,
            @RequestParam(required = false) Integer days) {
        return ResponseEntity.ok(service.getRadiationByPatient(patientId, days));
    }

    @GetMapping("/api/radiation-logs/treatment/{treatmentId}")
    public ResponseEntity<List<RadiationLogResponse>> getRadiationByTreatment(@PathVariable Integer treatmentId) {
        return ResponseEntity.ok(service.getRadiationByTreatment(treatmentId));
    }
}
