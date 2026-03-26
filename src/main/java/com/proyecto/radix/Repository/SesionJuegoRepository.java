package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.SesionJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionJuegoRepository extends JpaRepository<SesionJuego, Integer> {
}
