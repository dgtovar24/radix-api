package com.project.radix.Repository;

import com.project.radix.Model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findByIsActiveTrue();
    Optional<Patient> findByFkUserId(Integer fkUserId);
    Optional<Patient> findByFkDoctorId(Integer fkDoctorId);
    Optional<Patient> findByFamilyAccessCode(String familyAccessCode);
}