-- RADIX API Database Schema
-- Generated from JPA Entities

-- 1. Master Tables (No Dependencies)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'Doctor',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE isotope_catalog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(50),
    type VARCHAR(100),
    half_life DOUBLE,
    half_life_unit VARCHAR(50)
);

CREATE TABLE units (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(50) NOT NULL
);

-- 2. Patient Profile
CREATE TABLE patients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    family_access_code VARCHAR(100) UNIQUE,
    fk_user_id INT,
    fk_doctor_id INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (fk_doctor_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 3. Smartwatch Device
CREATE TABLE smartwatches (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    imei VARCHAR(100) UNIQUE NOT NULL,
    mac_address VARCHAR(100) UNIQUE NOT NULL,
    model VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- 4. Core System: Treatments
CREATE TABLE treatments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    fk_doctor_id INT NOT NULL,
    fk_radioisotope_id INT NOT NULL,
    fk_smartwatch_id INT,
    fk_unit_id INT NOT NULL,
    room INT,
    initial_dose DOUBLE,
    safety_threshold DOUBLE,
    isolation_days INT,
    start_date DATETIME NOT NULL,
    end_date DATETIME,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_doctor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_radioisotope_id) REFERENCES isotope_catalog(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_smartwatch_id) REFERENCES smartwatches(id) ON DELETE SET NULL,
    FOREIGN KEY (fk_unit_id) REFERENCES units(id) ON DELETE CASCADE
);

-- 5. Logs and Monitoring
CREATE TABLE radiation_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    fk_treatment_id INT,
    radiation_level DOUBLE NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_treatment_id) REFERENCES treatments(id) ON DELETE SET NULL
);

CREATE TABLE health_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    bpm INT,
    steps INT,
    distance DOUBLE,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE health_metrics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_treatment_id INT,
    fk_patient_id INT,
    bpm INT,
    steps INT,
    distance DOUBLE,
    current_radiation DOUBLE,
    recorded_at DATETIME,
    FOREIGN KEY (fk_treatment_id) REFERENCES treatments(id) ON DELETE SET NULL,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE SET NULL
);

-- 6. Interaction and App
CREATE TABLE doctor_alerts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    fk_treatment_id INT NOT NULL,
    alert_type VARCHAR(100),
    message TEXT,
    is_resolved BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    FOREIGN KEY (fk_treatment_id) REFERENCES treatments(id) ON DELETE CASCADE
);

CREATE TABLE motivational_messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    message_text TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE game_sessions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    score INT DEFAULT 0,
    level_reached INT DEFAULT 1,
    played_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

CREATE TABLE settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fk_patient_id INT NOT NULL,
    unit_preference VARCHAR(50) DEFAULT 'metric',
    theme VARCHAR(50) DEFAULT 'light',
    notifications_enabled BOOLEAN DEFAULT TRUE,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (fk_patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Sample Data Inserts
-- Users (Doctors/Admins)
INSERT INTO users (first_name, last_name, email, password, role) VALUES
('John', 'Smith', 'john.smith@radix.com', 'password123', 'admin'),
('Sarah', 'Johnson', 'sarah.johnson@radix.com', 'password123', 'doctor'),
('Michael', 'Williams', 'michael.williams@radix.com', 'password123', 'doctor'),
('Emily', 'Brown', 'emily.brown@radix.com', 'password123', 'doctor'),
('David', 'Jones', 'david.jones@radix.com', 'password123', 'doctor');

-- Patients
INSERT INTO patients (full_name, phone, address, is_active, family_access_code, fk_user_id, fk_doctor_id) VALUES
('Robert Garcia', '+1-555-0101', '123 Main St, New York, NY', TRUE, 'FAM-ROBT-001', 1, 2),
('Maria Lopez', '+1-555-0102', '456 Oak Ave, Los Angeles, CA', TRUE, 'FAM-MARIA-002', 1, 2),
('James Miller', '+1-555-0103', '789 Pine Rd, Chicago, IL', TRUE, 'FAM-JAMES-003', 1, 3),
('Linda Davis', '+1-555-0104', '321 Elm St, Houston, TX', TRUE, 'FAM-LINDA-004', 1, 3),
('William Wilson', '+1-555-0105', '654 Maple Dr, Phoenix, AZ', TRUE, 'FAM-WILL-005', 1, 4),
('Elizabeth Moore', '+1-555-0106', '987 Cedar Ln, Philadelphia, PA', TRUE, 'FAM-ELIZ-006', 1, 4),
('Thomas Taylor', '+1-555-0107', '147 Birch Way, San Antonio, TX', TRUE, 'FAM-THOM-007', 1, 5),
('Nancy Anderson', '+1-555-0108', '258 Spruce St, San Diego, CA', TRUE, 'FAM-NANC-008', 1, 5);

-- Isotope Catalog
INSERT INTO isotope_catalog (name, symbol, type, half_life, half_life_unit) VALUES
('Iodine-131', 'I-131', 'Radioiodine', 192.96, 'hours'),
('Technetium-99m', 'Tc-99m', 'Medical Imaging', 6.02, 'hours'),
('Cobalt-60', 'Co-60', 'Teletherapy', 5.27, 'years'),
('Radium-226', 'Ra-226', 'Brachytherapy', 1600, 'years'),
('Phosphorus-32', 'P-32', 'Research', 14.29, 'days'),
('Strontium-90', 'Sr-90', 'Industrial', 28.8, 'years'),
('Yttrium-90', 'Y-90', 'Therapeutic', 64.1, 'hours'),
('Lutetium-177', 'Lu-177', 'Therapeutic', 6.65, 'days');

-- Units
INSERT INTO units (name, symbol) VALUES
('Millicurie', 'mCi'),
('Microcurie', 'µCi'),
('Becquerel', 'Bq'),
('Curie', 'Ci'),
('Gray', 'Gy'),
('Sievert', 'Sv'),
('rad', 'rad'),
('rem', 'rem');

-- Smartwatches
INSERT INTO smartwatches (fk_patient_id, imei, mac_address, model, is_active) VALUES
(1, 'IMEI-001-001-001', 'MAC-00:00:00:01:01:01', 'Apple Watch Series 9', TRUE),
(2, 'IMEI-002-002-002', 'MAC-00:00:00:02:02:02', 'Samsung Galaxy Watch 6', TRUE),
(3, 'IMEI-003-003-003', 'MAC-00:00:00:03:03:03', 'Fitbit Sense 2', TRUE),
(4, 'IMEI-004-004-004', 'MAC-00:00:00:04:04:04', 'Garmin Venu 3', TRUE),
(5, 'IMEI-005-005-005', 'MAC-00:00:00:05:05:05', 'Apple Watch Ultra 2', TRUE),
(6, 'IMEI-006-006-006', 'MAC-00:00:00:06:06:06', 'Samsung Galaxy Watch 5', TRUE),
(7, 'IMEI-007-007-007', 'MAC-00:00:00:07:07:07', 'Google Pixel Watch 2', TRUE),
(8, 'IMEI-008-008-008', 'MAC-00:00:00:08:08:08', 'Fitbit Versa 4', TRUE);

-- Treatments
INSERT INTO treatments (fk_patient_id, fk_doctor_id, fk_radioisotope_id, fk_smartwatch_id, fk_unit_id, room, initial_dose, safety_threshold, isolation_days, start_date, is_active) VALUES
(1, 2, 1, 1, 1, 101, 150.0, 200.0, 3, '2026-04-01 09:00:00', TRUE),
(2, 2, 1, 2, 1, 102, 175.0, 220.0, 4, '2026-04-03 10:00:00', TRUE),
(3, 3, 7, 3, 1, 103, 100.0, 180.0, 2, '2026-04-05 08:00:00', TRUE),
(4, 3, 1, 4, 1, 104, 200.0, 250.0, 5, '2026-04-07 11:00:00', TRUE),
(5, 4, 7, 5, 1, 105, 125.0, 190.0, 3, '2026-04-10 09:30:00', TRUE),
(6, 4, 1, 6, 1, 106, 160.0, 210.0, 4, '2026-04-12 10:00:00', TRUE),
(7, 5, 7, 7, 1, 107, 110.0, 170.0, 2, '2026-04-14 08:30:00', TRUE),
(8, 5, 1, 8, 1, 108, 185.0, 230.0, 4, '2026-04-15 09:00:00', TRUE);

-- Radiation Logs
INSERT INTO radiation_logs (fk_patient_id, fk_treatment_id, radiation_level, timestamp) VALUES
(1, 1, 150.0, '2026-04-01 09:00:00'),
(1, 1, 145.2, '2026-04-01 12:00:00'),
(1, 1, 138.7, '2026-04-01 18:00:00'),
(2, 2, 175.0, '2026-04-03 10:00:00'),
(2, 2, 168.3, '2026-04-03 14:00:00'),
(3, 3, 100.0, '2026-04-05 08:00:00'),
(3, 3, 95.5, '2026-04-05 12:00:00'),
(4, 4, 200.0, '2026-04-07 11:00:00'),
(5, 5, 125.0, '2026-04-10 09:30:00');

-- Health Logs
INSERT INTO health_logs (fk_patient_id, bpm, steps, distance, timestamp) VALUES
(1, 72, 5000, 3.5, '2026-04-01 10:00:00'),
(1, 75, 8000, 5.8, '2026-04-01 15:00:00'),
(2, 68, 4500, 3.2, '2026-04-03 11:00:00'),
(2, 70, 7000, 5.0, '2026-04-03 16:00:00'),
(3, 78, 6000, 4.2, '2026-04-05 09:00:00'),
(4, 65, 3500, 2.5, '2026-04-07 12:00:00'),
(5, 80, 9000, 6.5, '2026-04-10 10:00:00');

-- Health Metrics
INSERT INTO health_metrics (fk_treatment_id, fk_patient_id, bpm, steps, distance, current_radiation, recorded_at) VALUES
(1, 1, 72, 5000, 3.5, 145.2, '2026-04-01 12:00:00'),
(2, 2, 68, 4500, 3.2, 168.3, '2026-04-03 14:00:00'),
(3, 3, 78, 6000, 4.2, 95.5, '2026-04-05 12:00:00'),
(4, 4, 65, 3500, 2.5, 195.0, '2026-04-07 18:00:00'),
(5, 5, 80, 9000, 6.5, 120.0, '2026-04-10 15:00:00');

-- Doctor Alerts
INSERT INTO doctor_alerts (fk_patient_id, fk_treatment_id, alert_type, message, is_resolved) VALUES
(1, 1, 'RADIATION_HIGH', 'Patient radiation level approaching safety threshold', FALSE),
(4, 4, 'MISSING_DATA', 'No health data received from smartwatch in 24 hours', FALSE),
(5, 5, 'HEART_RATE_HIGH', 'Patient heart rate exceeded 100 BPM for 30 minutes', TRUE);

-- Motivational Messages
INSERT INTO motivational_messages (fk_patient_id, message_text, is_read, sent_at) VALUES
(1, 'Great job completing your morning walk today! Keep it up!', TRUE, '2026-04-01 08:00:00'),
(1, 'Remember to stay hydrated during your treatment.', FALSE, '2026-04-01 14:00:00'),
(2, 'You are doing amazing! Only 2 more days of treatment.', FALSE, '2026-04-03 09:00:00'),
(3, 'Your radiation levels are decreasing faster than expected!', TRUE, '2026-04-05 16:00:00'),
(4, 'Please remember to wear your smartwatch at all times.', FALSE, '2026-04-07 10:00:00'),
(5, 'Congratulations on reaching the halfway point of your treatment!', TRUE, '2026-04-10 12:00:00');

-- Game Sessions
INSERT INTO game_sessions (fk_patient_id, score, level_reached, played_at) VALUES
(1, 850, 5, '2026-04-01 16:00:00'),
(1, 920, 6, '2026-04-02 16:00:00'),
(2, 780, 4, '2026-04-03 17:00:00'),
(3, 1100, 8, '2026-04-05 18:00:00'),
(4, 650, 3, '2026-04-07 19:00:00'),
(5, 980, 7, '2026-04-10 17:00:00'),
(6, 720, 4, '2026-04-12 18:00:00'),
(7, 1050, 7, '2026-04-14 17:00:00'),
(8, 890, 5, '2026-04-15 16:00:00');

-- Settings
INSERT INTO settings (fk_patient_id, unit_preference, theme, notifications_enabled) VALUES
(1, 'metric', 'light', TRUE),
(2, 'imperial', 'dark', TRUE),
(3, 'metric', 'light', FALSE),
(4, 'metric', 'dark', TRUE),
(5, 'imperial', 'light', TRUE),
(6, 'metric', 'light', TRUE),
(7, 'imperial', 'dark', FALSE),
(8, 'metric', 'light', TRUE);
