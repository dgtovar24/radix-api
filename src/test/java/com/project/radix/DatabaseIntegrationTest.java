package com.project.radix;

import com.project.radix.Model.*;
import com.project.radix.Repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DatabaseIntegrationTest {

    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private TreatmentRepository treatmentRepository;
    @Autowired private IsotopeCatalogRepository isotopeCatalogRepository;
    @Autowired private UnitRepository unitRepository;
    @Autowired private SmartwatchRepository smartwatchRepository;
    @Autowired private RadiationLogRepository radiationLogRepository;
    @Autowired private HealthLogRepository healthLogRepository;
    @Autowired private DoctorAlertRepository doctorAlertRepository;
    @Autowired private MotivationalMessageRepository motivationalMessageRepository;
    @Autowired private GameSessionRepository gameSessionRepository;
    @Autowired private SettingsRepository settingsRepository;

    @Test
    void testUserCRUD() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Doctor");
        user.setEmail("test.doctor@radix.com");
        user.setPassword("password123");
        user.setRole("doctor");
        
        user = userRepository.save(user);
        assertNotNull(user.getId());
        
        Optional<User> found = userRepository.findByEmail("test.doctor@radix.com");
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getFirstName());
        
        assertTrue(userRepository.existsByEmail("test.doctor@radix.com"));
        
        userRepository.delete(user);
        assertFalse(userRepository.existsByEmail("test.doctor@radix.com"));
    }

    @Test
    void testPatientCRUD() {
        User doctor = createUser("Dr", "Patient", "dr.patient@radix.com", "doctor");
        
        Patient patient = new Patient();
        patient.setFullName("Test Patient");
        patient.setPhone("+1-555-9999");
        patient.setAddress("123 Test St");
        patient.setIsActive(true);
        patient.setFamilyAccessCode("TEST-FAM-001");
        patient.setFkDoctorId(doctor.getId());
        
        patient = patientRepository.save(patient);
        assertNotNull(patient.getId());
        assertEquals("Test Patient", patient.getFullName());
        
        List<Patient> activePatients = patientRepository.findByIsActiveTrue();
        assertTrue(activePatients.size() > 0);
        
        Optional<Patient> byDoctor = patientRepository.findByFkDoctorId(doctor.getId());
        assertTrue(byDoctor.isPresent());
        
        patientRepository.delete(patient);
    }

    @Test
    void testIsotopeCatalog() {
        IsotopeCatalog isotope = new IsotopeCatalog();
        isotope.setName("Test Isotope");
        isotope.setSymbol("Ti-99");
        isotope.setType("Test");
        isotope.setHalfLife(72.0);
        isotope.setHalfLifeUnit("hours");
        
        isotope = isotopeCatalogRepository.save(isotope);
        assertNotNull(isotope.getId());
        
        List<IsotopeCatalog> all = isotopeCatalogRepository.findAll();
        assertTrue(all.size() > 0);
        
        isotopeCatalogRepository.delete(isotope);
    }

    @Test
    void testUnits() {
        Unit unit = new Unit();
        unit.setName("Test Unit");
        unit.setSymbol("TU");
        
        unit = unitRepository.save(unit);
        assertNotNull(unit.getId());
        
        List<Unit> all = unitRepository.findAll();
        assertTrue(all.size() > 0);
        
        unitRepository.delete(unit);
    }

    @Test
    void testSmartwatch() {
        User user = createUser("Watch", "Owner", "watch.owner@radix.com", "doctor");
        Patient patient = createPatient("Watch Patient", "FAM-WATCH-001", user.getId());
        
        Smartwatch smartwatch = new Smartwatch();
        smartwatch.setFkPatientId(patient.getId());
        smartwatch.setImei("IMEI-TEST-123");
        smartwatch.setMacAddress("MAC:TEST:123");
        smartwatch.setModel("Test Watch X1");
        smartwatch.setIsActive(true);
        
        smartwatch = smartwatchRepository.save(smartwatch);
        assertNotNull(smartwatch.getId());
        
        Optional<Smartwatch> byImei = smartwatchRepository.findByImei("IMEI-TEST-123");
        assertTrue(byImei.isPresent());
        
        smartwatchRepository.delete(smartwatch);
    }

    @Test
    @Disabled("H2 sequence isolation issue - IDs conflict when DataLoader seeds isotopes first")
    void testTreatment() {
        User doctor = createUser("Dr", "Treatment", "dr.treatment@radix.com", "doctor");
        Patient patient = createPatient("Treatment Patient", "FAM-TREAT-001", doctor.getId());
        IsotopeCatalog isotope = createIsotope("Treatment Isotope", "Ti-99");
        Unit unit = createUnit("Treatment Unit", "TU");
        
        Treatment treatment = new Treatment();
        treatment.setFkPatientId(patient.getId());
        treatment.setFkDoctorId(doctor.getId());
        treatment.setFkRadioisotopeId(isotope.getId());
        treatment.setFkUnitId(unit.getId());
        treatment.setRoom(101);
        treatment.setInitialDose(150.0);
        treatment.setSafetyThreshold(200.0);
        treatment.setIsolationDays(3);
        treatment.setStartDate(LocalDateTime.now());
        treatment.setIsActive(true);
        
        treatment = treatmentRepository.save(treatment);
        assertNotNull(treatment.getId());
        
        List<Treatment> byPatient = treatmentRepository.findByFkPatientId(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        List<Treatment> byDoctor = treatmentRepository.findByFkDoctorId(doctor.getId());
        assertTrue(byDoctor.size() > 0);
        
        List<Treatment> active = treatmentRepository.findByIsActiveTrue();
        assertTrue(active.size() > 0);
        
        treatmentRepository.delete(treatment);
    }

    @Test
    void testRadiationLog() {
        User doctor = createUser("Dr", "Radiation", "dr.radiation@radix.com", "doctor");
        Patient patient = createPatient("Radiation Patient", "FAM-RAD-001", doctor.getId());
        
        RadiationLog log = new RadiationLog();
        log.setFkPatientId(patient.getId());
        log.setRadiationLevel(150.5);
        log.setTimestamp(LocalDateTime.now());
        
        log = radiationLogRepository.save(log);
        assertNotNull(log.getId());
        
        List<RadiationLog> byPatient = radiationLogRepository.findByFkPatientIdOrderByTimestampDesc(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        radiationLogRepository.delete(log);
    }

    @Test
    void testHealthLog() {
        User doctor = createUser("Dr", "Health", "dr.health@radix.com", "doctor");
        Patient patient = createPatient("Health Patient", "FAM-HEALTH-001", doctor.getId());
        
        HealthLog log = new HealthLog();
        log.setFkPatientId(patient.getId());
        log.setBpm(72);
        log.setSteps(5000);
        log.setDistance(3.5);
        log.setTimestamp(LocalDateTime.now());
        
        log = healthLogRepository.save(log);
        assertNotNull(log.getId());
        
        List<HealthLog> byPatient = healthLogRepository.findByFkPatientIdOrderByTimestampDesc(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        healthLogRepository.delete(log);
    }

    @Test
    @Disabled("H2 sequence isolation issue - IDs conflict when DataLoader seeds isotopes first")
    void testDoctorAlert() {
        User doctor = createUser("Dr", "Alert", "dr.alert@radix.com", "doctor");
        Patient patient = createPatient("Alert Patient", "FAM-ALERT-001", doctor.getId());
        Treatment treatment = createTreatment(patient.getId(), doctor.getId());
        
        DoctorAlert alert = new DoctorAlert();
        alert.setFkPatientId(patient.getId());
        alert.setFkTreatmentId(treatment.getId());
        alert.setAlertType("TEST_ALERT");
        alert.setMessage("This is a test alert");
        alert.setIsResolved(false);
        
        alert = doctorAlertRepository.save(alert);
        assertNotNull(alert.getId());
        
        List<DoctorAlert> unresolved = doctorAlertRepository.findByIsResolvedFalse();
        assertTrue(unresolved.size() > 0);
        
        List<DoctorAlert> byPatient = doctorAlertRepository.findByFkPatientId(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        doctorAlertRepository.delete(alert);
    }

    @Test
    void testMotivationalMessage() {
        User doctor = createUser("Dr", "Motiv", "dr.motiv@radix.com", "doctor");
        Patient patient = createPatient("Motiv Patient", "FAM-MOTIV-001", doctor.getId());
        
        MotivationalMessage msg = new MotivationalMessage();
        msg.setFkPatientId(patient.getId());
        msg.setMessageText("Keep going! You're doing great!");
        msg.setIsRead(false);
        
        msg = motivationalMessageRepository.save(msg);
        assertNotNull(msg.getId());
        
        List<MotivationalMessage> unread = motivationalMessageRepository.findByIsReadFalseAndFkPatientId(patient.getId());
        assertTrue(unread.size() > 0);
        
        List<MotivationalMessage> byPatient = motivationalMessageRepository.findByFkPatientIdOrderBySentAtDesc(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        motivationalMessageRepository.delete(msg);
    }

    @Test
    void testGameSession() {
        User doctor = createUser("Dr", "Game", "dr.game@radix.com", "doctor");
        Patient patient = createPatient("Game Patient", "FAM-GAME-001", doctor.getId());
        
        GameSession session = new GameSession();
        session.setFkPatientId(patient.getId());
        session.setScore(850);
        session.setLevelReached(5);
        
        session = gameSessionRepository.save(session);
        assertNotNull(session.getId());
        
        List<GameSession> byPatient = gameSessionRepository.findByFkPatientIdOrderByPlayedAtDesc(patient.getId());
        assertTrue(byPatient.size() > 0);
        
        gameSessionRepository.delete(session);
    }

    @Test
    void testSettings() {
        User doctor = createUser("Dr", "Settings", "dr.settings@radix.com", "doctor");
        Patient patient = createPatient("Settings Patient", "FAM-SETTINGS-001", doctor.getId());
        
        Settings settings = new Settings();
        settings.setFkPatientId(patient.getId());
        settings.setUnitPreference("metric");
        settings.setTheme("dark");
        settings.setNotificationsEnabled(true);
        
        settings = settingsRepository.save(settings);
        assertNotNull(settings.getId());
        
        Optional<Settings> byPatient = settingsRepository.findByFkPatientId(patient.getId());
        assertTrue(byPatient.isPresent());
        assertEquals("dark", byPatient.get().getTheme());
        
        settingsRepository.delete(settings);
    }

    @Test
    @Disabled("H2 sequence isolation issue - IDs conflict when DataLoader seeds isotopes first")
    void testAllRepositoriesCanSaveAndRetrieve() {
        User user = createUser("Repo", "Test", "repo.test@radix.com", "doctor");
        Patient patient = createPatient("Repo Test Patient", "FAM-REPO-001", user.getId());
        IsotopeCatalog isotope = createIsotope("Repo Isotope", "RI-99");
        Unit unit = createUnit("Repo Unit", "RU");
        Smartwatch smartwatch = createSmartwatch(patient.getId(), "IMEI-REPO-123", "MAC:REPO:123");
        Treatment treatment = createTreatment(patient.getId(), user.getId());
        RadiationLog radLog = createRadiationLog(patient.getId());
        HealthLog healthLog = createHealthLog(patient.getId());
        DoctorAlert alert = createDoctorAlert(patient.getId(), treatment.getId());
        MotivationalMessage msg = createMotivationalMessage(patient.getId());
        GameSession game = createGameSession(patient.getId());
        Settings settings = createSettings(patient.getId());
        
        assertNotNull(user.getId());
        assertNotNull(patient.getId());
        assertNotNull(isotope.getId());
        assertNotNull(unit.getId());
        assertNotNull(smartwatch.getId());
        assertNotNull(treatment.getId());
        assertNotNull(radLog.getId());
        assertNotNull(healthLog.getId());
        assertNotNull(alert.getId());
        assertNotNull(msg.getId());
        assertNotNull(game.getId());
        assertNotNull(settings.getId());
        
        assertTrue(userRepository.count() > 0);
        assertTrue(patientRepository.count() > 0);
    }

    private User createUser(String firstName, String lastName, String email, String role) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword("password123");
        user.setRole(role);
        return userRepository.save(user);
    }

    private Patient createPatient(String fullName, String familyCode, Integer doctorId) {
        Patient patient = new Patient();
        patient.setFullName(fullName);
        patient.setFamilyAccessCode(familyCode);
        patient.setIsActive(true);
        patient.setFkDoctorId(doctorId);
        return patientRepository.save(patient);
    }

    private IsotopeCatalog createIsotope(String name, String symbol) {
        IsotopeCatalog isotope = new IsotopeCatalog();
        isotope.setName(name);
        isotope.setSymbol(symbol);
        isotope.setType("Test");
        isotope.setHalfLife(72.0);
        isotope.setHalfLifeUnit("hours");
        return isotopeCatalogRepository.save(isotope);
    }

    private Unit createUnit(String name, String symbol) {
        Unit unit = new Unit();
        unit.setName(name);
        unit.setSymbol(symbol);
        return unitRepository.save(unit);
    }

    private Smartwatch createSmartwatch(Integer patientId, String imei, String mac) {
        Smartwatch smartwatch = new Smartwatch();
        smartwatch.setFkPatientId(patientId);
        smartwatch.setImei(imei);
        smartwatch.setMacAddress(mac);
        smartwatch.setModel("Test");
        smartwatch.setIsActive(true);
        return smartwatchRepository.save(smartwatch);
    }

    private Treatment createTreatment(Integer patientId, Integer doctorId) {
        IsotopeCatalog isotope = createIsotope("Treat Isotope", "Ti");
        Unit unit = createUnit("Treat Unit", "TU");
        Treatment treatment = new Treatment();
        treatment.setFkPatientId(patientId);
        treatment.setFkDoctorId(doctorId);
        treatment.setFkRadioisotopeId(isotope.getId());
        treatment.setFkUnitId(unit.getId());
        treatment.setStartDate(LocalDateTime.now());
        treatment.setIsActive(true);
        treatment.setInitialDose(100.0);
        treatment.setSafetyThreshold(10.0);
        treatment.setIsolationDays(3);
        return treatmentRepository.save(treatment);
    }

    private RadiationLog createRadiationLog(Integer patientId) {
        RadiationLog log = new RadiationLog();
        log.setFkPatientId(patientId);
        log.setRadiationLevel(100.0);
        log.setTimestamp(LocalDateTime.now());
        return radiationLogRepository.save(log);
    }

    private HealthLog createHealthLog(Integer patientId) {
        HealthLog log = new HealthLog();
        log.setFkPatientId(patientId);
        log.setBpm(70);
        log.setSteps(1000);
        log.setDistance(0.8);
        log.setTimestamp(LocalDateTime.now());
        return healthLogRepository.save(log);
    }

    private DoctorAlert createDoctorAlert(Integer patientId, Integer treatmentId) {
        DoctorAlert alert = new DoctorAlert();
        alert.setFkPatientId(patientId);
        alert.setFkTreatmentId(treatmentId);
        alert.setAlertType("TEST");
        alert.setMessage("Test message");
        alert.setIsResolved(false);
        return doctorAlertRepository.save(alert);
    }

    private MotivationalMessage createMotivationalMessage(Integer patientId) {
        MotivationalMessage msg = new MotivationalMessage();
        msg.setFkPatientId(patientId);
        msg.setMessageText("Test message");
        msg.setIsRead(false);
        return motivationalMessageRepository.save(msg);
    }

    private GameSession createGameSession(Integer patientId) {
        GameSession session = new GameSession();
        session.setFkPatientId(patientId);
        session.setScore(100);
        session.setLevelReached(1);
        return gameSessionRepository.save(session);
    }

    private Settings createSettings(Integer patientId) {
        Settings settings = new Settings();
        settings.setFkPatientId(patientId);
        settings.setTheme("light");
        settings.setUnitPreference("metric");
        settings.setNotificationsEnabled(true);
        return settingsRepository.save(settings);
    }
}
