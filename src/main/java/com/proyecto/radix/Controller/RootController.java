package com.proyecto.radix.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
            "app", "Radix API",
            "status", "running",
            "version", "0.0.1"
        );
    }
}
