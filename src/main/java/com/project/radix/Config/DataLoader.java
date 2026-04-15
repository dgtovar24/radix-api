package com.project.radix.Config;

import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
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
        };
    }
}
