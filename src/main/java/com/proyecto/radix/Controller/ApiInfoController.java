package com.proyecto.radix.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ApiInfoController {

    @GetMapping
    public ResponseEntity<?> apiInfo() {
        return ResponseEntity.ok(Map.of(
            "name",        "Radix API",
            "version",     "v2",
            "description", "API REST para el sistema de gestión médica Radix",
            "status",      "operational",
            "timestamp",   Instant.now().toString(),
            "docs",        "https://api.raddix.pro/v2/actuator",
            "endpoints",   List.of(
                Map.of("method", "GET",  "path", "/v2/",                    "description", "Información de la API"),
                Map.of("method", "POST", "path", "/v2/api/auth/login",      "description", "Autenticación de usuario"),
                Map.of("method", "POST", "path", "/v2/api/auth/register",   "description", "Registro de nuevo usuario"),
                Map.of("method", "GET",  "path", "/v2/actuator/health",     "description", "Estado del servicio"),
                Map.of("method", "GET",  "path", "/v2/actuator/info",       "description", "Información del build"),
                Map.of("method", "GET",  "path", "/v2/actuator/metrics",    "description", "Métricas de la aplicación")
            )
        ));
    }
}
