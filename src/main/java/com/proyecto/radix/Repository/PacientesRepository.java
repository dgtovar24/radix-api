package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.Pacientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PacientesRepository extends JpaRepository<Pacientes, Integer> {
    Optional<Pacientes> findByIdPacienteAndActivoTrue(Integer id);
    List<Pacientes> findAllByActivoTrue();

    Optional<Pacientes> findByCuentaUsuarioId(Integer idUsuario);
}
