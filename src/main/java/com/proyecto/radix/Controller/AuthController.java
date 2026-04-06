package com.proyecto.radix.Controller;

import com.proyecto.radix.DTO.LoginRequest;
import com.proyecto.radix.DTO.RegisterRequest;
import com.proyecto.radix.Model.Usuario;
import com.proyecto.radix.Repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        String email = body.getEmail();
        String password = body.getPassword();

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty() || !usuario.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Usuario u = usuario.get();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "firstName", u.getFirstName(),
                "role", u.getRole()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest body) {
        if (usuarioRepository.existsByEmail(body.getEmail())) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        String role = body.getRole();
        if (role == null || (!role.equals("Doctor") && !role.equals("Paciente"))) {
            return ResponseEntity.status(400).body(Map.of("error", "Role must be 'Doctor' or 'Paciente'"));
        }

        Usuario u = new Usuario();
        u.setFirstName(body.getNombre());
        u.setLastName(body.getApellido());
        u.setEmail(body.getEmail());
        u.setPassword(body.getPassword());
        u.setRole(role);

        usuarioRepository.save(u);
        return ResponseEntity.ok(Map.of("message", "User created"));
    }

    @GetMapping("/pacientes")
    public ResponseEntity<?> getAllPacientes() {
        return ResponseEntity.ok(usuarioRepository.findAll().stream()
                .filter(u -> "Paciente".equals(u.getRole()))
                .map(u -> Map.of(
                        "id", u.getId(),
                        "firstName", u.getFirstName(),
                        "lastName", u.getLastName(),
                        "email", u.getEmail()
                ))
                .toList());
    }
}
