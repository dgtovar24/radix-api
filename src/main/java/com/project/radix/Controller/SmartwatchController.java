package com.project.radix.Controller;

import com.project.radix.DTO.SmartwatchRequest;
import com.project.radix.DTO.SmartwatchResponse;
import com.project.radix.Service.SmartwatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/smartwatches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SmartwatchController {

    private final SmartwatchService smartwatchService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SmartwatchRequest request) {
        try {
            SmartwatchResponse response = smartwatchService.create(request);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<SmartwatchResponse>> getAll() {
        return ResponseEntity.ok(smartwatchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(smartwatchService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SmartwatchResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(smartwatchService.getByPatient(patientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody SmartwatchRequest request) {
        try {
            return ResponseEntity.ok(smartwatchService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        try {
            smartwatchService.deactivate(id);
            return ResponseEntity.ok(Map.of("message", "Smartwatch deactivated"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}
