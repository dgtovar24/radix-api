package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.DispositivoReloj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispositivoRelojRepository extends JpaRepository<DispositivoReloj, Integer> {
}
