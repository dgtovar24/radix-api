package com.proyecto.radix.Repository;

import com.proyecto.radix.Model.MetricasSalud;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MetricasSaludRepository extends JpaRepository<MetricasSalud, Integer> {
    List<MetricasSalud> findByTreatmentIdOrderByRecordedAtDesc(Integer treatmentId, Pageable pageable);
}
