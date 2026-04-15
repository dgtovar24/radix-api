package com.project.radix.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping({"", "/"})
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(Map.of(
            "name",        "Radix API",
            "version",     "v2",
            "description", "REST API for Radix medical management system",
            "status",      "operational",
            "timestamp",   Instant.now().toString(),
            "docs",        "https://api.raddix.pro/v2/actuator",
            "endpoints",   List.of(
                Map.of("method", "GET",  "path", "/v2/",                        "description", "API info & status"),
                Map.of("method", "POST", "path", "/v2/api/auth/login",           "description", "User authentication"),
                Map.of("method", "POST", "path", "/v2/api/auth/register",        "description", "User registration"),
                Map.of("method", "GET",  "path", "/v2/api/pacientes",            "description", "List all patients"),
                Map.of("method", "GET",  "path", "/v2/api/pacientes/{id}",       "description", "Get patient by ID"),
                Map.of("method", "GET",  "path", "/v2/actuator/health",          "description", "Service health check"),
                Map.of("method", "GET",  "path", "/v2/actuator/info",            "description", "Build information"),
                Map.of("method", "GET",  "path", "/v2/actuator/metrics",         "description", "Application metrics")
            )
        ));
    }
}
