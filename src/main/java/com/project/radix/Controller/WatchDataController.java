package com.project.radix.Controller;

import com.project.radix.DTO.WatchIngestRequest;
import com.project.radix.DTO.WatchMetricsResponse;
import com.project.radix.Model.OAuthClient;
import com.project.radix.Repository.OAuthClientRepository;
import com.project.radix.Service.WatchDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/watch")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WatchDataController {

    private final WatchDataService watchDataService;
    private final OAuthClientRepository oauthClientRepository;

    @PostMapping("/ingest")
    public ResponseEntity<?> ingestData(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody WatchIngestRequest request) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Optional<OAuthClient> clientOpt = oauthClientRepository.findByClientId(token);
            if (clientOpt.isEmpty() || !clientOpt.get().getIsActive()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
            }
        }

        try {
            watchDataService.ingestData(request);
            return ResponseEntity.ok(Map.of("status", "received"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{imei}/metrics")
    public ResponseEntity<List<WatchMetricsResponse>> getMetrics(@PathVariable String imei) {
        return ResponseEntity.ok(watchDataService.getMetricsByImei(imei));
    }

    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<?> getLatestMetrics(@PathVariable Integer patientId) {
        return watchDataService.getLatestMetricsByPatient(patientId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}