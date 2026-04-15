package com.project.radix.Repository;

import com.project.radix.Model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    List<Treatment> findByFkPatientId(Integer fkPatientId);
    List<Treatment> findByFkDoctorId(Integer fkDoctorId);
    List<Treatment> findByIsActiveTrue();
}