package com.project.radix.Controller;

import com.project.radix.DTO.DoctorResponse;
import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DoctorController {

    private final UserRepository userRepository;

    private DoctorResponse toResponse(User u) {
        return DoctorResponse.builder()
                .id(u.getId())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .role(u.getRole())
                .licenseNumber(u.getLicenseNumber())
                .specialty(u.getSpecialty())
                .createdAt(u.getCreatedAt())
                .build();
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAll() {
        List<DoctorResponse> doctors = userRepository.findByRole("Doctor").stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .filter(u -> "Doctor".equalsIgnoreCase(u.getRole()))
                .map(u -> ResponseEntity.ok((Object) toResponse(u)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Doctor not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return userRepository.findById(id)
                .filter(u -> "Doctor".equalsIgnoreCase(u.getRole()))
                .map(u -> {
                    if (body.containsKey("phone")) u.setPhone(body.get("phone"));
                    if (body.containsKey("licenseNumber")) u.setLicenseNumber(body.get("licenseNumber"));
                    if (body.containsKey("specialty")) u.setSpecialty(body.get("specialty"));
                    userRepository.save(u);
                    return ResponseEntity.ok((Object) toResponse(u));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Doctor not found")));
    }
}
