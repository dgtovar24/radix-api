package com.project.radix.Config;

import com.project.radix.Model.*;
import com.project.radix.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Configuration
@Profile("!test")
public class DataLoader {

    private final Random rand = new Random();

    @Bean
    public CommandLineRunner initDatabase(
            UserRepository userRepo,
            PatientRepository patientRepo,
            IsotopeCatalogRepository isotopeRepo,
            UnitRepository unitRepo,
            SmartwatchRepository smartwatchRepo,
            TreatmentRepository treatmentRepo,
            DoctorAlertRepository alertRepo,
            HealthMetricsRepository metricsRepo,
            HealthLogRepository healthLogRepo,
            RadiationLogRepository radiationRepo,
            MotivationalMessageRepository messageRepo,
            GameSessionRepository gameRepo,
            SettingsRepository settingsRepo,
            OAuthClientRepository oauthRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.count() > 0) { System.out.println("DB already seeded, skipping."); return; }

            System.out.println("====== SEEDING DATABASE ======");

            // ─── USERS ───
            User admin = user("Radix", "Admin", "Radix", "prgtest", "ADMIN", null, null, "600111000", passwordEncoder);
            User drElena = user("Elena", "Ruiz", "elena.ruiz@radix.pro", "pass123", "Doctor",
                    "282864321", "Medicina Nuclear", "600111001", passwordEncoder);
            User drMarc = user("Marc", "Vidal", "marc.vidal@radix.pro", "pass123", "Doctor",
                    "282864322", "Oncología Radioterápica", "600111002", passwordEncoder);
            User drInes = user("Inés", "Ferrer", "ines.ferrer@radix.pro", "pass123", "Doctor",
                    "282864323", "Radiofísica Hospitalaria", "600111003", passwordEncoder);
            User drPablo = user("Pablo", "Torres", "pablo.torres@radix.pro", "pass123", "Doctor",
                    "282864324", "Endocrinología", "600111004", passwordEncoder);
            User drCarmen = user("Carmen", "Navarro", "carmen.navarro@radix.pro", "pass123", "Doctor",
                    "282864325", "Medicina Nuclear", "600111005", passwordEncoder);
            User drDiego = user("Diego", "Alonso", "diego.alonso@radix.pro", "pass123", "Doctor",
                    "282864326", "Oncología Radioterápica", "600111006", passwordEncoder);
            userRepo.saveAll(List.of(admin, drElena, drMarc, drInes, drPablo, drCarmen, drDiego));
            System.out.println("  -> 7 users created (1 admin + 6 doctors)");

            // ─── PATIENTS ───
            String[][] patientData = {
                {"Ana", "García"}, {"Carlos", "Martínez"}, {"Elena", "Rodríguez"},
                {"David", "Fernández"}, {"María", "López"}, {"Javier", "Sánchez"},
                {"Laura", "Gómez"}, {"Pablo", "Díaz"}, {"Sofía", "Ruiz"},
                {"Diego", "Alonso-P"}, {"Carmen", "Navarro-P"}, {"Raúl", "Torres"}
            };
            User[] doctors = {drElena, drMarc, drInes, drPablo, drCarmen, drDiego};
            String[] phones = {"+34 612 345 6%02d", "+34 623 456 7%02d", "+34 634 567 8%02d",
                    "+34 645 678 9%02d", "+34 656 789 0%02d", "+34 667 890 1%02d",
                    "+34 678 901 2%02d", "+34 689 012 3%02d", "+34 690 123 4%02d",
                    "+34 601 234 5%02d", "+34 602 345 6%02d", "+34 603 456 7%02d"};
            String[] addresses = {"Calle Mayor 1, Madrid", "Av. Diagonal 234, Barcelona", "Plaza España 5, Valencia",
                    "Calle Real 78, Sevilla", "Gran Vía 56, Madrid", "Rambla Catalunya 12, Barcelona",
                    "Calle Serrano 89, Madrid", "Av. Constitución 45, Málaga", "Paseo Marítimo 23, Cádiz",
                    "Calle Alcalá 100, Madrid", "Calle Colón 7, Valencia", "Calle San Juan 34, Bilbao"};

            for (int i = 0; i < patientData.length; i++) {
                String fullName = patientData[i][0] + " " + patientData[i][1];
                User u = userRepo.save(user(patientData[i][0], patientData[i][1],
                        patientData[i][0].toLowerCase() + "@paciente.com", "pass123", "patient",
                        null, null, String.format(phones[i], i + 10), passwordEncoder));
                Patient p = new Patient();
                p.setFullName(fullName);
                p.setPhone(String.format(phones[i], i + 10));
                p.setAddress(addresses[i]);
                p.setIsActive(true);
                p.setFamilyAccessCode("FAM-" + String.format("%04d", i + 1));
                p.setFkUserId(u.getId());
                p.setFkDoctorId(doctors[i % doctors.length].getId());
                p.setCreatedAt(LocalDateTime.now().minusDays(28 - i));
                patientRepo.save(p);
            }
            System.out.println("  -> 12 patients created");

            // ─── UNITS ───
            if (unitRepo.count() == 0) {
                String[][] unitData = {{"Milicurio", "mCi"}, {"Becquerel", "Bq"}, {"Gray", "Gy"},
                        {"Sievert", "Sv"}, {"Rad", "rad"}, {"Rem", "rem"},
                        {"Roentgen", "R"}, {"Coulomb/kg", "C/kg"}};
                for (String[] u : unitData) {
                    Unit unit = new Unit(); unit.setName(u[0]); unit.setSymbol(u[1]); unitRepo.save(unit);
                }
                System.out.println("  -> 8 units created");
            }

            // ─── ISOTOPES ───
            if (isotopeRepo.count() == 0) {
                List<IsotopeCatalog> isotopes = List.of(
                    iso("Yodo-131", "I-131", "Beta/Gamma", 8.02, "días"),
                    iso("Tecnecio-99m", "Tc-99m", "Gamma", 6.01, "horas"),
                    iso("Radio-223", "Ra-223", "Alfa", 11.4, "días"),
                    iso("Lutecio-177", "Lu-177", "Beta/Gamma", 6.65, "días"),
                    iso("Itrio-90", "Y-90", "Beta", 2.67, "días"),
                    iso("Samario-153", "Sm-153", "Beta/Gamma", 1.95, "días"),
                    iso("Holmio-166", "Ho-166", "Beta/Gamma", 1.12, "días"),
                    iso("Renio-186", "Re-186", "Beta/Gamma", 3.7, "días"),
                    iso("Renio-188", "Re-188", "Beta/Gamma", 0.71, "días"),
                    iso("Estroncio-89", "Sr-89", "Beta", 50.5, "días"),
                    iso("Fósforo-32", "P-32", "Beta", 14.3, "días")
                );
                isotopeRepo.saveAll(isotopes);
                System.out.println("  -> 11 isotopes created");
            }

            // ─── SMARTWATCHES ───
            List<Patient> patients = patientRepo.findAll();
            List<IsotopeCatalog> isotopes = isotopeRepo.findAll();
            String[] models = {"RadixWatch Pro v2", "RadixWatch Lite", "RadixWatch Pro v2",
                    "External Generic", "RadixWatch Pro v2", "RadixWatch Lite",
                    "RadixWatch Pro v2", "External Generic", "RadixWatch Lite",
                    "RadixWatch Pro v2", "RadixWatch Lite", "RadixWatch Pro v2"};
            for (int i = 0; i < patients.size(); i++) {
                Smartwatch sw = new Smartwatch();
                sw.setFkPatientId(patients.get(i).getId());
                sw.setImei("3592810849" + String.format("%04d", 2011 + i));
                sw.setMacAddress(String.format("00:1B:44:11:3A:%02X", 0xB0 + i));
                sw.setModel(models[i]);
                sw.setIsActive(true);
                smartwatchRepo.save(sw);
            }
            System.out.println("  -> 12 smartwatches created");

            // ─── TREATMENTS ───
            double[] doses = {150.0, 100.0, 200.0, 75.0, 180.0, 120.0, 160.0, 90.0, 140.0, 110.0, 130.0, 170.0};
            int[] rooms = {402, 405, 410, 401, 403, 408, 411, 404, 407, 409, 406, 412};

            for (int i = 0; i < patients.size(); i++) {
                IsotopeCatalog iso = isotopes.get(i % isotopes.size());
                Treatment t = new Treatment();
                t.setFkPatientId(patients.get(i).getId());
                t.setFkDoctorId(doctors[i % doctors.length].getId());
                t.setFkRadioisotopeId(iso.getId());
                t.setFkSmartwatchId(null);
                t.setFkUnitId(null);
                t.setRoom(rooms[i]);
                t.setInitialDose(doses[i]);
                t.setSafetyThreshold(doses[i] * 0.1);
                t.setIsolationDays((int) Math.ceil(iso.getHalfLife() * 10));
                t.setStartDate(LocalDateTime.now().minusDays(i + 1));
                t.setIsActive(i < 8); // 8 active, 4 completed
                if (i >= 8) t.setEndDate(LocalDateTime.now().minusDays(1));
                treatmentRepo.save(t);
            }
            System.out.println("  -> 12 treatments (8 active, 4 completed)");

            // ─── ALERTS ───
            List<Treatment> treatments = treatmentRepo.findAll();
            String[][] alertDefs = {
                {"RADIATION_HIGH", "Nivel de radiación supera el umbral de seguridad", "false"},
                {"BPM_IRREGULAR", "Frecuencia cardíaca irregular detectada", "false"},
                {"LOW_ACTIVITY", "Actividad física por debajo del mínimo recomendado", "true"},
                {"OUTSIDE_ZONE", "Paciente detectado fuera de la zona de aislamiento", "false"},
                {"RADIATION_HIGH", "Pico de radiación detectado en la habitación", "false"},
                {"BPM_IRREGULAR", "Bradicardia detectada", "true"},
                {"LOW_ACTIVITY", "Sin registro de pasos en las últimas 24h", "false"},
                {"RADIATION_HIGH", "Alerta de seguridad en sala de aislamiento", "false"},
                {"DEVICE_OFFLINE", "Smartwatch sin conexión desde hace 2 horas", "false"},
                {"BPM_IRREGULAR", "Taquicardia detectada", "true"},
                {"LOW_ACTIVITY", "Nivel de movilidad críticamente bajo", "false"},
                {"RADIATION_HIGH", "Superación del threshold de seguridad", "false"},
                {"DEVICE_OFFLINE", "Smartwatch batería crítica (<5%)", "true"},
                {"OUTSIDE_ZONE", "Alerta de geolocalización fuera del perímetro", "false"},
                {"BPM_IRREGULAR", "Arritmia detectada en monitor cardíaco", "false"},
                {"RADIATION_HIGH", "Radiación ambiental elevada en sala", "false"},
                {"LOW_ACTIVITY", "Paciente sin movimiento en 12 horas", "true"},
                {"DEVICE_OFFLINE", "Pérdida de señal del dispositivo wearable", "true"},
                {"RADIATION_HIGH", "Nivel crítico de radiación — evacuar sala", "false"},
                {"BPM_IRREGULAR", "Frecuencia cardíaca anómala sostenida", "false"}
            };

            for (int i = 0; i < alertDefs.length; i++) {
                Patient p = patients.get(i % patients.size());
                Treatment tr = treatments.get(i % treatments.size());
                DoctorAlert a = new DoctorAlert();
                a.setFkPatientId(p.getId());
                a.setFkTreatmentId(tr.getId());
                a.setAlertType(alertDefs[i][0]);
                a.setMessage(alertDefs[i][1]);
                a.setIsResolved(Boolean.parseBoolean(alertDefs[i][2]));
                a.setCreatedAt(LocalDateTime.now().minusHours(i).minusMinutes(rand.nextInt(60)));
                alertRepo.save(a);
            }
            System.out.println("  -> 20 alerts created (8 pending, 12 resolved)");

            // ─── HEALTH METRICS (7 days per patient) ───
            for (int d = 7; d >= 0; d--) {
                for (Patient p : patients) {
                    HealthMetrics m = new HealthMetrics();
                    m.setFkPatientId(p.getId());
                    m.setFkTreatmentId(treatments.get(patients.indexOf(p) % treatments.size()).getId());
                    m.setBpm(60 + rand.nextInt(40));
                    m.setSteps(2000 + rand.nextInt(5000));
                    m.setDistance(1.0 + rand.nextDouble() * 5);
                    m.setCurrentRadiation(doses[patients.indexOf(p)] * 0.1 * (1.0 - d * 0.12) + rand.nextDouble());
                    m.setRecordedAt(LocalDateTime.now().minusDays(d).minusHours(rand.nextInt(12)));
                    metricsRepo.save(m);
                }
            }
            System.out.println("  -> " + (8 * 12) + " health metrics records created");

            // ─── RADIATION LOGS (7 days per patient) ───
            for (int d = 7; d >= 0; d--) {
                for (int i = 0; i < patients.size(); i++) {
                    RadiationLog rl = new RadiationLog();
                    rl.setFkPatientId(patients.get(i).getId());
                    rl.setFkTreatmentId(treatments.get(i % treatments.size()).getId());
                    rl.setRadiationLevel(doses[i] * 0.1 * (1.0 - d * 0.12) + rand.nextDouble() * 2);
                    rl.setTimestamp(LocalDateTime.now().minusDays(d).withHour(8 + rand.nextInt(12)));
                    radiationRepo.save(rl);
                }
            }
            System.out.println("  -> " + (8 * 12) + " radiation logs created");

            // ─── HEALTH LOGS ───
            for (Patient p : patients) {
                for (int d = 0; d < 5; d++) {
                    HealthLog hl = new HealthLog();
                    hl.setFkPatientId(p.getId());
                    hl.setBpm(60 + rand.nextInt(40));
                    hl.setSteps(2000 + rand.nextInt(6000));
                    hl.setDistance(1.5 + rand.nextDouble() * 4);
                    hl.setTimestamp(LocalDateTime.now().minusHours(d * 8).minusMinutes(rand.nextInt(60)));
                    healthLogRepo.save(hl);
                }
            }
            System.out.println("  -> " + (5 * 12) + " health logs created");

            // ─── MOTIVATIONAL MESSAGES ───
            String[] messages = {
                "¡Ánimo! Ya has completado el 25% de tu tratamiento. Sigue así.",
                "Recuerda mantenerte hidratado durante el confinamiento.",
                "Tu médico ha revisado tus métricas. Todo va según lo previsto.",
                "Paciente estable. Continúa con las recomendaciones.",
                "Has alcanzado tu objetivo de pasos diarios. ¡Excelente!",
                "Recordatorio: cita de control mañana a las 10:00 AM.",
                "Tus niveles de radiación están dentro del rango esperado.",
                "El equipo médico te envía un saludo. Estamos contigo.",
                "Recuerda tomar la medicación según la pauta establecida.",
                "Buen trabajo con la actividad física. Sigue así.",
                "Tu frecuencia cardíaca se mantiene estable. Buenas noticias.",
                "Mañana es tu último día de confinamiento. ¡Ya casi está!",
                "El smartwatch está funcionando correctamente.",
                "No olvides cargar el dispositivo wearable antes de dormir.",
                "Tus familiares te envían un mensaje de apoyo.",
                "Revisión de tratamiento programada para esta semana."
            };
            for (int i = 0; i < messages.length; i++) {
                MotivationalMessage msg = new MotivationalMessage();
                msg.setFkPatientId(patients.get(i % patients.size()).getId());
                msg.setMessageText(messages[i]);
                msg.setIsRead(i > 10);
                msg.setSentAt(LocalDateTime.now().minusHours(i * 2).minusMinutes(rand.nextInt(30)));
                messageRepo.save(msg);
            }
            System.out.println("  -> 16 messages created");

            // ─── GAME SESSIONS ───
            for (Patient p : patients) {
                int sessions = 2 + rand.nextInt(4);
                for (int s = 0; s < sessions; s++) {
                    GameSession gs = new GameSession();
                    gs.setFkPatientId(p.getId());
                    gs.setScore(100 + rand.nextInt(2000));
                    gs.setLevelReached(1 + rand.nextInt(10));
                    gs.setPlayedAt(LocalDateTime.now().minusDays(rand.nextInt(7)).minusHours(rand.nextInt(12)));
                    gameRepo.save(gs);
                }
            }
            System.out.println("  -> game sessions created");

            // ─── PATIENT SETTINGS ───
            String[] themes = {"light", "dark", "light", "dark", "light", "light", "dark", "light", "dark", "light", "dark", "light"};
            for (int i = 0; i < patients.size(); i++) {
                Settings s = new Settings();
                s.setFkPatientId(patients.get(i).getId());
                s.setUnitPreference(i % 2 == 0 ? "metric" : "imperial");
                s.setTheme(themes[i]);
                s.setNotificationsEnabled(i != 3);
                s.setUpdatedAt(LocalDateTime.now());
                settingsRepo.save(s);
            }
            System.out.println("  -> 12 patient settings created");

            // ─── OAUTH CLIENTS ───
            for (Patient p : patients) {
                if (!oauthRepo.existsByClientId(p.getFamilyAccessCode())) {
                    OAuthClient client = new OAuthClient();
                    client.setClientId(p.getFamilyAccessCode());
                    client.setClientSecret("secret_" + p.getId());
                    client.setClientName("Smartwatch-" + p.getFullName());
                    client.setGrantType("client_credentials");
                    client.setScopes("read write");
                    client.setIsActive(true);
                    client.setFkUserId(p.getFkUserId());
                    oauthRepo.save(client);
                }
            }
            System.out.println("  -> 12 OAuth clients created");

            System.out.println("====== DATABASE SEEDED SUCCESSFULLY ======");
        };
    }

    private User user(String firstName, String lastName, String email, String password, String role,
                      String license, String specialty, String phone, PasswordEncoder encoder) {
        User u = new User();
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        u.setLicenseNumber(license);
        u.setSpecialty(specialty);
        u.setPhone(phone);
        return u;
    }

    private IsotopeCatalog iso(String name, String symbol, String type, Double halfLife, String unit) {
        IsotopeCatalog i = new IsotopeCatalog();
        i.setName(name);
        i.setSymbol(symbol);
        i.setType(type);
        i.setHalfLife(halfLife);
        i.setHalfLifeUnit(unit);
        return i;
    }
}
