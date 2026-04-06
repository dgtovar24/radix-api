package com.proyecto.radix.Controller;

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
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String contrasena = body.get("contrasena");

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty() || !usuario.get().getContrasena().equals(contrasena)) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
        }

        Usuario u = usuario.get();
        return ResponseEntity.ok(Map.of(
                "id", u.getId(),
                "nombre", u.getNombre(),
                "rol", u.getRol()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        if (usuarioRepository.existsByEmail(body.get("email"))) {
            return ResponseEntity.status(400).body(Map.of("error", "Email ya existe"));
        }

        Usuario u = new Usuario();
        u.setNombre(body.get("nombre"));
        u.setApellido(body.get("apellido"));
        u.setEmail(body.get("email"));
        u.setContrasena(body.get("contrasena"));
        u.setRol("Doctor");

        usuarioRepository.save(u);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario creado"));
    }
}
