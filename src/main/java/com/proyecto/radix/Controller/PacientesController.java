package com.proyecto.radix.Controller;

import com.proyecto.radix.Model.Pacientes;
import com.proyecto.radix.Model.Usuario;
import com.proyecto.radix.Repository.PacientesRepository;
import com.proyecto.radix.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PacientesController {

    private final PacientesRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;

    //Endpoint app:
    @PostMapping("/alta")
    public ResponseEntity<?> altaPaciente(@RequestBody Map<String, String> body) {

        if (usuarioRepository.existsByEmail(body.get("email"))) {
            return ResponseEntity.status(400).body(Map.of("error", "Email already exists"));
        }

        Usuario u = new Usuario();
        u.setFirstName(body.get("firstName"));
        u.setLastName(body.get("lastName"));
        u.setEmail(body.get("email"));
        u.setPassword(body.get("password"));
        u.setRole("Paciente");

        usuarioRepository.save(u);

        Pacientes p = new Pacientes();
        p.setNombreCompleto(body.get("firstName") + " " + body.get("lastName"));
        p.setCuentaUsuario(u);
        p.setActivo(true);

        if(body.containsKey("medicId")) {
            Usuario medico = usuarioRepository.findById(Integer.parseInt(body.get("medicId"))).orElse(null);
            p.setMedico(medico);
        }

        pacienteRepository.save(p);

        return ResponseEntity.ok(Map.of("message", "Patient created successfully"));
    }

    //Enpoint reloj:
    @GetMapping("/perfil/{idUsuario}")
    public ResponseEntity<?> getPerfilPorUsuario(@PathVariable Integer idUsuario) {
        return pacienteRepository.findByCuentaUsuarioId(idUsuario)
                .map(p -> ResponseEntity.ok(Map.of(
                        "idPaciente", p.getIdPaciente(),
                        "nombreCompleto", p.getNombreCompleto()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Paciente no encontrado")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaciente(@PathVariable Integer id) {
        return pacienteRepository.findByIdPacienteAndActivoTrue(id)
                .map(p -> ResponseEntity.ok(Map.of(
                        "id", p.getIdPaciente(),
                        "nombreCompleto", p.getNombreCompleto()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "No encontrado")));
    }

    @GetMapping
    public ResponseEntity<?> getAllPacientes() {
        List<?> pacientes = pacienteRepository.findAllByActivoTrue().stream()
                .map(p -> Map.of(
                        "id", p.getIdPaciente(),
                        "nombreCompleto", p.getNombreCompleto()
                ))
                .toList();
        return ResponseEntity.ok(pacientes);
    }
}
