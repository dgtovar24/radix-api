package com.project.radix.Repository;

import com.project.radix.Model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Integer> {
    List<GameSession> findByFkPatientIdOrderByPlayedAtDesc(Integer fkPatientId);
}