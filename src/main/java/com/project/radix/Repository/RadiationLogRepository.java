package com.project.radix.Repository;

import com.project.radix.Model.RadiationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RadiationLogRepository extends JpaRepository<RadiationLog, Integer> {
    List<RadiationLog> findByFkPatientIdOrderByTimestampDesc(Integer fkPatientId);
    List<RadiationLog> findByFkTreatmentIdOrderByTimestampDesc(Integer fkTreatmentId);
    List<RadiationLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}