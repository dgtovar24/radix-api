package com.project.radix.Controller;

import com.project.radix.DTO.RegisterRequest;
import com.project.radix.Model.Patient;
import com.project.radix.Model.User;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public AuthController(UserRepository userRepository, PatientRepository patientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Email and password are required"));
        }

        // We use either email or the username (Radix for admin)
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User u = user.get();
        return ResponseEntity.ok(Map.of(
                "token", u.getId(), // Fake token using ID as the session mock architecture
                "id", u.getId(),
                "firstName", u.getFirstName(),
                "role", u.getRole()
        ));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(
            @RequestHeader("Authorization") String creatorAuth,
            @RequestBody RegisterRequest request) {
        
        Optional<User> creatorOpt = resolveTokenUser(creatorAuth);
        
        if (creatorOpt.isEmpty() || !"ADMIN".equalsIgnoreCase(creatorOpt.get().getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Only Admin can create Doctors"));
        }

        if (request.getFirstName() == null || request.getLastName() == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Fields firstName, lastName, email, and password are required"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        User doctor = new User();
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPassword(request.getPassword());
        doctor.setRole("DOCTOR");
        userRepository.save(doctor);
        
        return ResponseEntity.ok(Map.of("message", "Doctor user created successfully", "id", doctor.getId()));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<?> registerPatient(
            @RequestHeader("Authorization") String creatorAuth,
            @RequestBody RegisterRequest request) {
            
        Optional<User> creatorOpt = resolveTokenUser(creatorAuth);

        if (creatorOpt.isEmpty() || !"DOCTOR".equalsIgnoreCase(creatorOpt.get().getRole())) {
            return ResponseEntity.status(403).body(Map.of("error", "Only a Doctor can register Patients"));
        }

        if (request.getFirstName() == null || request.getLastName() == null || request.getEmail() == null || request.getPassword() == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Fields firstName, lastName, email, and password are required"));
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        // 1. Create Patient User account
        User patientUser = new User();
        patientUser.setFirstName(request.getFirstName());
        patientUser.setLastName(request.getLastName());
        patientUser.setEmail(request.getEmail());
        patientUser.setPassword(request.getPassword());
        patientUser.setRole("PATIENT");
        patientUser = userRepository.save(patientUser);

        // 2. Create the specific Patient file entity linked to Doctor
        Patient patientRecord = new Patient();
        patientRecord.setFullName(request.getFirstName() + " " + request.getLastName());
        patientRecord.setPhone(request.getPhone());
        patientRecord.setAddress(request.getAddress());
        patientRecord.setFamilyAccessCode(UUID.randomUUID().toString());
        patientRecord.setFkUserId(patientUser.getId());
        patientRecord.setFkDoctorId(creatorOpt.get().getId());
        patientRepository.save(patientRecord);

        return ResponseEntity.ok(Map.of("message", "Patient registered successfully", "id", patientUser.getId()));
    }

    private Optional<User> resolveTokenUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return Optional.empty();
        try {
            // Simplified Token architecture: Extract user ID from "Bearer {userId}" placeholder mock
            Integer userId = Integer.parseInt(authHeader.substring(7).trim());
            return userRepository.findById(userId);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
