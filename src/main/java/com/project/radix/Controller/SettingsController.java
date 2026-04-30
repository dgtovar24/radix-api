package com.project.radix.Controller;

import com.project.radix.DTO.SettingsResponse;
import com.project.radix.Model.Settings;
import com.project.radix.Repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SettingsController {

    private final SettingsRepository settingsRepository;

    private SettingsResponse toResponse(Settings s) {
        return SettingsResponse.builder()
                .id(s.getId())
                .patientId(s.getFkPatientId())
                .unitPreference(s.getUnitPreference())
                .theme(s.getTheme())
                .notificationsEnabled(s.getNotificationsEnabled())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getByPatient(@PathVariable Integer patientId) {
        return settingsRepository.findByFkPatientId(patientId)
                .map(s -> ResponseEntity.ok((Object) toResponse(s)))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Settings not found")));
    }

    @PutMapping("/patient/{patientId}")
    public ResponseEntity<?> update(@PathVariable Integer patientId, @RequestBody Map<String, Object> body) {
        Settings s = settingsRepository.findByFkPatientId(patientId)
                .orElseGet(() -> {
                    Settings ns = new Settings();
                    ns.setFkPatientId(patientId);
                    return ns;
                });

        if (body.containsKey("unitPreference")) s.setUnitPreference((String) body.get("unitPreference"));
        if (body.containsKey("theme")) s.setTheme((String) body.get("theme"));
        if (body.containsKey("notificationsEnabled")) s.setNotificationsEnabled((Boolean) body.get("notificationsEnabled"));
        s.setUpdatedAt(LocalDateTime.now());

        s = settingsRepository.save(s);
        return ResponseEntity.ok(toResponse(s));
    }
}
