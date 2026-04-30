package com.project.radix.Controller;

import com.project.radix.DTO.PatientResponse;
import com.project.radix.Model.Patient;
import com.project.radix.Model.User;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    private PatientResponse toResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .phone(p.getPhone())
                .address(p.getAddress())
                .isActive(p.getIsActive())
                .familyAccessCode(p.getFamilyAccessCode())
                .fkUserId(p.getFkUserId())
                .fkDoctorId(p.getFkDoctorId())
                .createdAt(p.getCreatedAt())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody Map<String, String> body) {
        if (userRepository.existsByEmail(body.get("email"))) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        User u = new User();
        u.setFirstName(body.get("firstName"));
        u.setLastName(body.get("lastName"));
        u.setEmail(body.get("email"));
        u.setPassword(body.get("password"));
        u.setRole("patient");

        u = userRepository.save(u);

        Patient p = new Patient();
        p.setFullName(body.get("firstName") + " " + body.get("lastName"));
        p.setFkUserId(u.getId());
        p.setIsActive(true);
        p.setFamilyAccessCode("FAM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        if (body.containsKey("phone")) {
            p.setPhone(body.get("phone"));
        }
        if (body.containsKey("address")) {
            p.setAddress(body.get("address"));
        }
        if (body.containsKey("doctorId")) {
            p.setFkDoctorId(Integer.parseInt(body.get("doctorId")));
        }

        patientRepository.save(p);

        return ResponseEntity.ok(Map.of("message", "Patient created successfully", "userId", u.getId()));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfileByUser(@PathVariable Integer userId) {
        return patientRepository.findByFkUserId(userId)
                .map(p -> ResponseEntity.ok((Object) toResponse(p)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Patient not found")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatient(@PathVariable Integer id) {
        return patientRepository.findById(id)
                .filter(Patient::getIsActive)
                .map(p -> ResponseEntity.ok((Object) toResponse(p)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Not found")));
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        List<PatientResponse> patients = patientRepository.findByIsActiveTrue().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return patientRepository.findById(id)
                .map(p -> {
                    if (body.containsKey("phone")) p.setPhone(body.get("phone"));
                    if (body.containsKey("address")) p.setAddress(body.get("address"));
                    if (body.containsKey("familyAccessCode")) p.setFamilyAccessCode(body.get("familyAccessCode"));
                    if (body.containsKey("isActive")) p.setIsActive(Boolean.parseBoolean(body.get("isActive")));
                    patientRepository.save(p);
                    return ResponseEntity.ok((Object) toResponse(p));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Patient not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivatePatient(@PathVariable Integer id) {
        return patientRepository.findById(id)
                .map(p -> {
                    p.setIsActive(false);
                    patientRepository.save(p);
                    return ResponseEntity.ok(Map.of("message", "Patient deactivated"));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Patient not found")));
    }
}
