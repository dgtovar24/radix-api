package com.project.radix.Repository;

import com.project.radix.Model.HealthMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthMetricsRepository extends JpaRepository<HealthMetrics, Integer> {
    List<HealthMetrics> findByFkPatientIdOrderByRecordedAtDesc(Integer fkPatientId);
    Optional<HealthMetrics> findFirstByFkPatientIdOrderByRecordedAtDesc(Integer fkPatientId);
    List<HealthMetrics> findByFkTreatmentId(Integer fkTreatmentId);
}