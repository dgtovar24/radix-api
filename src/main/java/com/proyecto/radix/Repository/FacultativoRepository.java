package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.Facultativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultativoRepository extends JpaRepository<Facultativo, Integer> {
}
