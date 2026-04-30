package com.project.radix.Repository;

import com.project.radix.Model.Smartwatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SmartwatchRepository extends JpaRepository<Smartwatch, Integer> {
    Optional<Smartwatch> findByImei(String imei);
    Optional<Smartwatch> findByMacAddress(String macAddress);
    List<Smartwatch> findByFkPatientId(Integer fkPatientId);
}
