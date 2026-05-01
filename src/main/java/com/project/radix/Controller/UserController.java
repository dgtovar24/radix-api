package com.project.radix.Controller;

import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userRepository.findByRole(role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok((Object) u))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return userRepository.findById(id)
                .map(u -> {
                    if (body.containsKey("firstName")) u.setFirstName(body.get("firstName"));
                    if (body.containsKey("lastName")) u.setLastName(body.get("lastName"));
                    if (body.containsKey("email")) u.setEmail(body.get("email"));
                    if (body.containsKey("phone")) u.setPhone(body.get("phone"));
                    if (body.containsKey("licenseNumber")) u.setLicenseNumber(body.get("licenseNumber"));
                    if (body.containsKey("specialty")) u.setSpecialty(body.get("specialty"));
                    if (body.containsKey("role")) u.setRole(body.get("role"));
                    if (body.containsKey("password")) u.setPassword(body.get("password"));
                    userRepository.save(u);
                    return ResponseEntity.ok((Object) u);
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "User not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }
}
