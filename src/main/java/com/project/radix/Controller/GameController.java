package com.project.radix.Controller;

import com.project.radix.DTO.GameSessionRequest;
import com.project.radix.DTO.GameSessionResponse;
import com.project.radix.Model.GameSession;
import com.project.radix.Repository.GameSessionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GameController {

    private final GameSessionRepository gameRepository;

    private GameSessionResponse toResponse(GameSession g) {
        return GameSessionResponse.builder()
                .id(g.getId())
                .patientId(g.getFkPatientId())
                .score(g.getScore())
                .levelReached(g.getLevelReached())
                .playedAt(g.getPlayedAt())
                .build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<GameSessionResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(gameRepository.findByFkPatientIdOrderByPlayedAtDesc(patientId)
                .stream().map(this::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<GameSessionResponse> create(@Valid @RequestBody GameSessionRequest request) {
        GameSession g = new GameSession();
        g.setFkPatientId(request.getFkPatientId());
        g.setScore(request.getScore() != null ? request.getScore() : 0);
        g.setLevelReached(request.getLevelReached() != null ? request.getLevelReached() : 1);
        g = gameRepository.save(g);
        return ResponseEntity.status(201).body(toResponse(g));
    }
}
