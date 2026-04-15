package com.project.radix.Controller;

import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User u = user.get();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "firstName", u.getFirstName(),
                "role", u.getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        if (userRepository.existsByEmail(body.get("email"))) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        User u = new User();
        u.setFirstName(body.get("firstName"));
        u.setLastName(body.get("lastName"));
        u.setEmail(body.get("email"));
        u.setPassword(body.get("password"));
        u.setRole("Doctor");

        userRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "User created"));
    }
}