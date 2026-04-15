package com.project.radix.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/docs")
@CrossOrigin(origins = "*")
public class DocsController {

    @GetMapping
    public ResponseEntity<?> getApiDocs() {
        return ResponseEntity.ok(Map.of(
            "api", "Radix API v1",
            "description", "Complete API Schema with expected POST bodies and GET responses",
            "endpoints", List.of(
                Map.of(
                    "path", "/api/auth/login",
                    "method", "POST",
                    "description", "Login user to get session ID/Token",
                    "request_body", Map.of(
                        "email", "string (required)",
                        "password", "string (required)"
                    ),
                    "response", Map.of(
                        "token", "integer (ID as token)",
                        "id", "integer",
                        "firstName", "string",
                        "role", "string"
                    )
                ),
                Map.of(
                    "path", "/api/auth/register/doctor",
                    "method", "POST",
                    "description", "ADMIN ONLY: Register a new doctor",
                    "headers", "Authorization: Bearer <ADMIN_ID>",
                    "request_body", Map.of(
                        "firstName", "string (required)",
                        "lastName", "string (required)",
                        "email", "string (required)",
                        "password", "string (required)"
                    ),
                    "response", Map.of(
                        "message", "string",
                        "id", "integer"
                    )
                ),
                Map.of(
                    "path", "/api/auth/register/patient",
                    "method", "POST",
                    "description", "DOCTOR ONLY: Register a new patient",
                    "headers", "Authorization: Bearer <DOCTOR_ID>",
                    "request_body", Map.of(
                        "firstName", "string (required)",
                        "lastName", "string (required)",
                        "email", "string (required)",
                        "password", "string (required)",
                        "phone", "string (optional)",
                        "address", "string (optional)"
                    ),
                    "response", Map.of(
                        "message", "string",
                        "id", "integer"
                    )
                ),
                Map.of(
                    "path", "/api/patients",
                    "method", "GET",
                    "description", "Get all active patients",
                    "response", List.of(
                        Map.of(
                            "id", "integer",
                            "fullName", "string"
                        )
                    )
                ),
                Map.of(
                    "path", "/api/patients/{id}",
                    "method", "GET",
                    "description", "Get specific patient by internal ID",
                    "response", Map.of(
                        "id", "integer",
                        "fullName", "string"
                    )
                ),
                Map.of(
                    "path", "/api/patients/profile/{userId}",
                    "method", "GET",
                    "description", "Get specific patient by their User ID",
                    "response", Map.of(
                        "id", "integer",
                        "fullName", "string"
                    )
                ),
                Map.of(
                    "path", "/api/patients/register",
                    "method", "POST",
                    "description", "Legacy/alternative patient registration",
                    "request_body", Map.of(
                        "firstName", "string (required)",
                        "lastName", "string (required)",
                        "email", "string (required)",
                        "password", "string (required)",
                        "doctorId", "string (optional)"
                    ),
                    "response", Map.of(
                        "message", "string",
                        "userId", "integer"
                    )
                )
            )
        ));
    }
}
