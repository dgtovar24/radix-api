package com.project.radix.Controller;

import com.project.radix.DTO.TreatmentCreateRequest;
import com.project.radix.DTO.TreatmentResponse;
import com.project.radix.Service.TreatmentService;
import com.project.radix.Util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TreatmentController {

    private final TreatmentService treatmentService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<TreatmentResponse>> getAllTreatments() {
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TreatmentResponse>> getActiveTreatments() {
        return ResponseEntity.ok(treatmentService.getActiveTreatments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTreatment(@PathVariable Integer id) {
        return treatmentService.getTreatmentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<TreatmentResponse>> getTreatmentsByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(treatmentService.getTreatmentsByPatient(patientId));
    }

    @PostMapping
    public ResponseEntity<?> createTreatment(
            @RequestHeader("Authorization") String auth,
            @Valid @RequestBody TreatmentCreateRequest request) {
        Integer doctorId = resolveDoctorId(auth);
        if (doctorId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid authorization"));
        }
        TreatmentResponse treatment = treatmentService.createTreatment(request, doctorId);
        return ResponseEntity.ok(treatment);
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<?> endTreatment(@PathVariable Integer id) {
        return treatmentService.endTreatment(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private Integer resolveDoctorId(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) return null;
        String token = auth.substring(7).trim();
        return jwtUtil.getUserId(token).orElse(null);
    }
}