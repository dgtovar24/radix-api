package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.Radioisotopo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadioisotopoRepository extends JpaRepository<Radioisotopo, Integer> {
}
