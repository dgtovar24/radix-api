package com.project.radix.Controller;

import com.project.radix.DTO.AlertResponse;
import com.project.radix.Service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorAlertController {

    private final AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AlertResponse>> getPendingAlerts() {
        return ResponseEntity.ok(alertService.getPendingAlerts());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AlertResponse>> getAlertsByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(alertService.getAlertsByPatient(patientId));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolveAlert(@PathVariable Integer id) {
        return alertService.resolveAlert(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}