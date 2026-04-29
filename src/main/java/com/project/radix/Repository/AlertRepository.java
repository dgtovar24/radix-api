package com.project.radix.Repository;

import com.project.radix.Model.DoctorAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<DoctorAlert, Integer> {
    List<DoctorAlert> findByIsResolvedFalse();
    List<DoctorAlert> findByFkPatientId(Integer fkPatientId);
}