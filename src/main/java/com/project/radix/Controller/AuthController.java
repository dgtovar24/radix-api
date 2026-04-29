package com.project.radix.Controller;

import com.project.radix.DTO.RegisterRequest;
import com.project.radix.DTO.TokenRequest;
import com.project.radix.DTO.TokenResponse;
import com.project.radix.Model.OAuthClient;
import com.project.radix.Model.Patient;
import com.project.radix.Model.User;
import com.project.radix.Repository.OAuthClientRepository;
import com.project.radix.Repository.PatientRepository;
import com.project.radix.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final OAuthClientRepository oauthClientRepository;

    public AuthController(UserRepository userRepository, PatientRepository patientRepository, OAuthClientRepository oauthClientRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.oauthClientRepository = oauthClientRepository;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody TokenRequest request) {
        if (!"client_credentials".equals(request.getGrantType())) {
            return ResponseEntity.status(400).body(Map.of("error", "Unsupported grant type"));
        }

        if (request.getClientId() == null || request.getClientSecret() == null) {
            return ResponseEntity.status(400).body(Map.of("error", "client_id and client_secret are required"));
        }

        Optional<OAuthClient> clientOpt = oauthClientRepository.findByClientIdAndIsActiveTrue(request.getClientId());
        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid client credentials"));
        }

        OAuthClient client = clientOpt.get();
        if (!client.getClientSecret().equals(request.getClientSecret())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid client credentials"));
        }

        String accessToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        client.setExpiresAt(expiresAt);
        oauthClientRepository.save(client);

        Patient patient = null;
        if (client.getFkUserId() != null) {
            patient = patientRepository.findByFkUserId(client.getFkUserId()).orElse(null);
        }

        TokenResponse response = TokenResponse.builder()
            .accessToken(accessToken)
            .tokenType("Bearer")
            .expiresIn(86400)
            .scope(client.getScopes())
            .patientId(patient != null ? patient.getId() : null)
            .patientName(patient != null ? patient.getFullName() : null)
            .familyAccessCode(patient != null ? patient.getFamilyAccessCode() : null)
            .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Email and password are required"));
        }

        // Hardcoded Admin Check
        if ("Radix".equals(email) && "radixelmejor1".equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "token", "admin-hardcoded-token", // Use a static token for the hardcoded admin
                    "id", 0, // Special ID for hardcoded admin
                    "firstName", "Radix",
                    "role", "ADMIN"
            ));
        }

        // Database authentication for other users
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

        String token = authHeader.substring(7).trim();

        // Check for hardcoded admin token
        if ("admin-hardcoded-token".equals(token)) {
            User admin = new User();
            admin.setId(0); // Special ID for hardcoded admin
            admin.setRole("ADMIN");
            return Optional.of(admin);
        }

        try {
            // Simplified Token architecture: Extract user ID from "Bearer {userId}" placeholder mock
            return userRepository.findById(Integer.parseInt(token));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
