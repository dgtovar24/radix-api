package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.AlertaMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertaMedicoRepository extends JpaRepository<AlertaMedico, Integer> {
    List<AlertaMedico> findByResoltaFalseOrderByCreadaElDesc();
}
