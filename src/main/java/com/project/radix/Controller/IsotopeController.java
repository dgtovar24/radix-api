package com.project.radix.Controller;

import com.project.radix.DTO.IsotopeResponse;
import com.project.radix.Model.IsotopeCatalog;
import com.project.radix.Repository.IsotopeCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/isotopes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IsotopeController {

    private final IsotopeCatalogRepository isotopeRepository;

    @GetMapping
    public ResponseEntity<List<IsotopeResponse>> getAllIsotopes() {
        return ResponseEntity.ok(isotopeRepository.findAll().stream()
            .map(this::toResponse)
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IsotopeResponse> getIsotope(@PathVariable Integer id) {
        return isotopeRepository.findById(id)
            .map(this::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    private IsotopeResponse toResponse(IsotopeCatalog isotope) {
        return IsotopeResponse.builder()
            .id(isotope.getId())
            .name(isotope.getName())
            .symbol(isotope.getSymbol())
            .type(isotope.getType())
            .halfLife(isotope.getHalfLife())
            .halfLifeUnit(isotope.getHalfLifeUnit())
            .build();
    }
}