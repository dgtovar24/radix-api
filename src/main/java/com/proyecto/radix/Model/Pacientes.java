package com.proyecto.radix.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
public class Pacientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    private Boolean activo = true;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @ManyToOne
    @JoinColumn(name = "fk_id_medico")
    private Usuario medico;

    @OneToOne
    @JoinColumn(name = "fk_id_usuario", unique = true)
    private Usuario cuentaUsuario;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
