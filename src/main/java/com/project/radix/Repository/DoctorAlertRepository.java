package com.project.radix.Repository;

import com.project.radix.Model.DoctorAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorAlertRepository extends JpaRepository<DoctorAlert, Integer> {
    List<DoctorAlert> findByFkPatientId(Integer fkPatientId);
    List<DoctorAlert> findByFkTreatmentId(Integer fkTreatmentId);
    List<DoctorAlert> findByIsResolvedFalse();
}