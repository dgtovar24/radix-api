package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.MensajeMotivacional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeMotivacionalRepository extends JpaRepository<MensajeMotivacional, Integer> {
}
