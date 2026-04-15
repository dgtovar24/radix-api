package com.project.radix.Repository;

import com.project.radix.Model.MotivationalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MotivationalMessageRepository extends JpaRepository<MotivationalMessage, Integer> {
    List<MotivationalMessage> findByFkPatientIdOrderBySentAtDesc(Integer fkPatientId);
    List<MotivationalMessage> findByIsReadFalseAndFkPatientId(Integer fkPatientId);
}