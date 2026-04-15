package com.project.radix.Repository;

import com.project.radix.Model.HealthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthLogRepository extends JpaRepository<HealthLog, Integer> {
    List<HealthLog> findByFkPatientIdOrderByTimestampDesc(Integer fkPatientId);
    List<HealthLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}