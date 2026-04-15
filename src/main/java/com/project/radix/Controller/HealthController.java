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
            "version",     "v1",
            "description", "REST API for Radix medical management system",
            "status",      "operational",
            "timestamp",   Instant.now().toString(),
            "docs",        "https://api.aiflex.dev/v1/actuator",
            "endpoints",   List.of(
                Map.of("method", "GET",  "path", "/v1/",                        "description", "API info & status"),
                Map.of("method", "POST", "path", "/v1/api/auth/login",           "description", "User authentication"),
                Map.of("method", "POST", "path", "/v1/api/auth/register",        "description", "User registration"),
                Map.of("method", "GET",  "path", "/v1/api/pacientes",            "description", "List all patients"),
                Map.of("method", "GET",  "path", "/v1/api/pacientes/{id}",       "description", "Get patient by ID"),
                Map.of("method", "GET",  "path", "/v1/actuator/health",          "description", "Service health check"),
                Map.of("method", "GET",  "path", "/v1/actuator/info",            "description", "Build information"),
                Map.of("method", "GET",  "path", "/v1/actuator/metrics",         "description", "Application metrics")
            )
        ));
    }
}
