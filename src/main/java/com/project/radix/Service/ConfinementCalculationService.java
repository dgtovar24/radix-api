package com.project.radix.Service;

import com.project.radix.Model.IsotopeCatalog;
import com.project.radix.Repository.IsotopeCatalogRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfinementCalculationService {

    private final IsotopeCatalogRepository isotopeRepository;

    public ConfinementResult calculate(Integer isotopeId, Double initialDose) {
        IsotopeCatalog isotope = isotopeRepository.findById(isotopeId)
            .orElseThrow(() -> new RuntimeException("Isotope not found with id: " + isotopeId));

        double confinementDays = Math.ceil(isotope.getHalfLife() * 10);
        double safetyThreshold = initialDose * 0.1;

        return new ConfinementResult(
            isotope.getName(),
            isotope.getSymbol(),
            isotope.getHalfLife(),
            isotope.getHalfLifeUnit(),
            (int) confinementDays,
            safetyThreshold
        );
    }

    public record ConfinementResult(
        String isotopeName,
        String symbol,
        Double halfLife,
        String halfLifeUnit,
        int isolationDays,
        double safetyThreshold
    ) {}
}