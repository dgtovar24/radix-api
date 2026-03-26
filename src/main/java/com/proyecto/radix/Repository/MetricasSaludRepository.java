package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.MetricasSalud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricasSaludRepository extends JpaRepository<MetricasSalud, Integer> {
}
