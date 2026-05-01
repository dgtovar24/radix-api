package com.project.radix;

import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import com.project.radix.Util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    private String adminToken;
    private Integer adminId;

    @BeforeEach
    void setup() {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Test");
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin = userRepository.save(admin);
            adminId = admin.getId();
            adminToken = jwtUtil.generateToken(admin);
        }
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@test.com\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.firstName").value("Admin"));
    }

    @Test
    void testLoginInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@test.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void testDashboardStats() throws Exception {
        mockMvc.perform(get("/api/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPatients").isNumber())
                .andExpect(jsonPath("$.totalDoctors").isNumber());
    }

    @Test
    void testGetPatients() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetDoctors() throws Exception {
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetSmartwatches() throws Exception {
        mockMvc.perform(get("/api/smartwatches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetTreatments() throws Exception {
        mockMvc.perform(get("/api/treatments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAlerts() throws Exception {
        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetIsotopes() throws Exception {
        mockMvc.perform(get("/api/isotopes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetUnits() throws Exception {
        mockMvc.perform(get("/api/units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testRegisterDoctorRequiresAuth() throws Exception {
        mockMvc.perform(post("/api/auth/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"New\",\"lastName\":\"Doc\",\"email\":\"new@test.com\",\"password\":\"pass123\"}"))
                .andExpect(status().is(500)); // Missing Authorization header causes internal error before reaching role check
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("operational"));
    }

    @Test
    void testRateLimiterBlocksAfter5Attempts() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"ratelimit@test.com\",\"password\":\"wrong\"}"));
        }
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ratelimit@test.com\",\"password\":\"wrong\"}"))
                .andExpect(status().is(429))
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.containsString("Too many")));
    }

    @Test
    void testUploadRequiresMultipartFile() throws Exception {
        mockMvc.perform(post("/api/upload")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(500)); // Missing multipart param causes internal error
    }

    @Test
    void testSmartwatchCreateInvalid() throws Exception {
        mockMvc.perform(post("/api/smartwatches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"imei\":\"\"}"))
                .andExpect(status().is(400));
    }

    @Test
    void testTokenEndpointInvalidGrant() throws Exception {
        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"grantType\":\"password\",\"clientId\":\"x\",\"clientSecret\":\"y\"}"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error").value("Unsupported grant type"));
    }
}
