package com.project.radix.Config;

import com.project.radix.Model.*;
import com.project.radix.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepository,
            PatientRepository patientRepository,
            IsotopeCatalogRepository isotopeCatalogRepository,
            OAuthClientRepository oauthClientRepository) {
        return args -> {
            if (userRepository.findByEmail("Radix").isEmpty()) {
                User admin = new User();
                admin.setFirstName("Radix");
                admin.setLastName("Admin");
                admin.setEmail("Radix");
                admin.setPassword("prgtest");
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("====== SYSTEM ADMIN USER CREATED ======");
            }

            if (isotopeCatalogRepository.count() == 0) {
                List<IsotopeCatalog> isotopes = List.of(
                    createIsotope("I-131", "Iodine", "I", "131", 8.02, "days"),
                    createIsotope("Tc-99m", "Technetium", "Tc", "99m", 6.01, "hours"),
                    createIsotope("Ra-223", "Radium", "Ra", "223", 11.4, "days"),
                    createIsotope("Lu-177", "Lutetium", "Lu", "177", 6.65, "days"),
                    createIsotope("Y-90", "Yttrium", "Y", "90", 2.67, "days"),
                    createIsotope("Sm-153", "Samarium", "Sm", "153", 1.95, "days"),
                    createIsotope("Ho-166", "Holmium", "Ho", "166", 1.12, "days"),
                    createIsotope("Re-186", "Rhenium", "Re", "186", 3.7, "days"),
                    createIsotope("Re-188", "Rhenium", "Re", "188", 17.0, "hours"),
                    createIsotope("Sr-89", "Strontium", "Sr", "89", 50.5, "days"),
                    createIsotope("Sm-153", "Samarium", "Sm", "153", 1.95, "days")
                );
                isotopeCatalogRepository.saveAll(isotopes);
                System.out.println("====== ISOTOPE CATALOG SEEDED ======");
            }

            patientRepository.findAll().forEach(patient -> {
                if (patient.getFamilyAccessCode() != null && !oauthClientRepository.existsByClientId(patient.getFamilyAccessCode())) {
                    OAuthClient client = new OAuthClient();
                    client.setClientId(patient.getFamilyAccessCode());
                    client.setClientSecret(patient.getId() + "_secret");
                    client.setClientName("Smartwatch-" + patient.getFullName());
                    client.setGrantType("client_credentials");
                    client.setScopes("read write");
                    client.setIsActive(true);
                    client.setFkUserId(patient.getFkUserId());
                    oauthClientRepository.save(client);
                    System.out.println("====== OAUTH CLIENT CREATED FOR: " + patient.getFullName() + " ======");
                }
            });
        };
    }

    private IsotopeCatalog createIsotope(String name, String fullName, String symbol, String isotopeNumber, Double halfLife, String unit) {
        IsotopeCatalog iso = new IsotopeCatalog();
        iso.setName(name);
        iso.setSymbol(symbol);
        iso.setType(fullName);
        iso.setHalfLife(halfLife);
        iso.setHalfLifeUnit(unit);
        return iso;
    }
}