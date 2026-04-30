package com.project.radix.Controller;

import com.project.radix.Model.Unit;
import com.project.radix.Repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UnitController {

    private final UnitRepository unitRepository;

    @GetMapping
    public ResponseEntity<List<Unit>> getAll() {
        return ResponseEntity.ok(unitRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return unitRepository.findById(id)
                .map(u -> ResponseEntity.ok((Object) u))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Unit not found")));
    }
}
