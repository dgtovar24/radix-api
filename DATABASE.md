# RADIX API - Database Documentation

## Overview

The RADIX API uses a relational database to manage nuclear medicine treatments, patient monitoring, and health metrics tracking.

## Technology Stack

- **Database**: MySQL (production) / H2 (development)
- **ORM**: Hibernate/JPA
- **Schema Management**: `ddl-auto: update` (auto-creates tables from entities)

## Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ PATIENTS : "doctor"
    USERS ||--o{ PATIENTS : "user_account"
    USERS ||--o{ TREATMENTS : "prescribes"
    USERS ||--o{ DOCTOR_ALERTS : "receives"
    
    PATIENTS ||--o{ SMARTWATCHES : "wears"
    PATIENTS ||--o{ TREATMENTS : "receives"
    PATIENTS ||--o{ RADIATION_LOGS : "generates"
    PATIENTS ||--o{ HEALTH_LOGS : "generates"
    PATIENTS ||--o{ HEALTH_METRICS : "generates"
    PATIENTS ||--o{ DOCTOR_ALERTS : "triggers"
    PATIENTS ||--o{ MOTIVATIONAL_MESSAGES : "receives"
    PATIENTS ||--o{ GAME_SESSIONS : "plays"
    PATIENTS ||--o| SETTINGS : "has"
    
    TREATMENTS ||--o{ RADIATION_LOGS : "logs"
    TREATMENTS ||--o{ HEALTH_METRICS : "logs"
    TREATMENTS ||--o{ DOCTOR_ALERTS : "triggers"
    TREATMENTS }o--|| ISOTOPE_CATALOG : "uses"
    TREATMENTS }o--|| UNITS : "measures_in"
    TREATMENTS }o--|| SMARTWATCHES : "monitored_by"

    USERS {
        int id PK
        string firstName
        string lastName
        string email UK
        string password
        string role
        datetime createdAt
    }

    PATIENTS {
        int id PK
        string fullName
        string phone
        string address
        boolean isActive
        string familyAccessCode UK
        int fkUserId FK
        int fkDoctorId FK
        datetime createdAt
    }

    SMARTWATCHES {
        int id PK
        int fkPatientId FK
        string imei UK
        string macAddress UK
        string model
        boolean isActive
    }

    ISOTOPE_CATALOG {
        int id PK
        string name
        string symbol
        string type
        double halfLife
        string halfLifeUnit
    }

    UNITS {
        int id PK
        string name
        string symbol
    }

    TREATMENTS {
        int id PK
        int fkPatientId FK
        int fkDoctorId FK
        int fkRadioisotopeId FK
        int fkSmartwatchId FK
        int fkUnitId FK
        int room
        double initialDose
        double safetyThreshold
        int isolationDays
        datetime startDate
        datetime endDate
        boolean isActive
    }

    RADIATION_LOGS {
        int id PK
        int fkPatientId FK
        int fkTreatmentId FK
        double radiationLevel
        datetime timestamp
    }

    HEALTH_LOGS {
        int id PK
        int fkPatientId FK
        int bpm
        int steps
        double distance
        datetime timestamp
    }

    HEALTH_METRICS {
        int id PK
        int fkTreatmentId FK
        int fkPatientId FK
        int bpm
        int steps
        double distance
        double currentRadiation
        datetime recordedAt
    }

    DOCTOR_ALERTS {
        int id PK
        int fkPatientId FK
        int fkTreatmentId FK
        string alertType
        string message
        boolean isResolved
        datetime createdAt
    }

    MOTIVATIONAL_MESSAGES {
        int id PK
        int fkPatientId FK
        string messageText
        boolean isRead
        datetime sentAt
    }

    GAME_SESSIONS {
        int id PK
        int fkPatientId FK
        int score
        int levelReached
        datetime playedAt
    }

    SETTINGS {
        int id PK
        int fkPatientId FK UK
        string unitPreference
        string theme
        boolean notificationsEnabled
        datetime updatedAt
    }
```

## Table Definitions

### 1. users
Authentication and user management table.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| first_name | VARCHAR(255) | NOT NULL |
| last_name | VARCHAR(255) | NOT NULL |
| email | VARCHAR(255) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL |
| role | VARCHAR(50) | NOT NULL, DEFAULT 'Doctor' |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP |

**Roles**: `admin`, `doctor`, `patient` (future expansion)
**Note**: Table is named `users` (plural) because `user` is a reserved keyword in H2 database.

### 2. patients
Patient profile and medical information.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| full_name | VARCHAR(255) | NOT NULL |
| phone | VARCHAR(50) | NULL |
| address | VARCHAR(500) | NULL |
| is_active | BOOLEAN | DEFAULT TRUE |
| family_access_code | VARCHAR(100) | UNIQUE |
| fk_user_id | INT | FK → user.id (SET NULL) |
| fk_doctor_id | INT | FK → user.id (SET NULL) |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 3. smartwatches
Wearable device tracking for patients.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| imei | VARCHAR(100) | UNIQUE, NOT NULL |
| mac_address | VARCHAR(100) | UNIQUE, NOT NULL |
| model | VARCHAR(100) | NULL |
| is_active | BOOLEAN | DEFAULT TRUE |

### 4. isotope_catalog
Radioisotope reference data for treatments.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| name | VARCHAR(255) | NOT NULL |
| symbol | VARCHAR(50) | NULL |
| type | VARCHAR(100) | NULL |
| half_life | DOUBLE | NULL |
| half_life_unit | VARCHAR(50) | NULL |

### 5. units
Measurement units for radiation doses.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| name | VARCHAR(255) | NOT NULL |
| symbol | VARCHAR(50) | NOT NULL |

### 6. treatments
Nuclear medicine treatment plans.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| fk_doctor_id | INT | FK → user.id (CASCADE) |
| fk_radioisotope_id | INT | FK → isotope_catalog.id (CASCADE) |
| fk_smartwatch_id | INT | FK → smartwatches.id (SET NULL) |
| fk_unit_id | INT | FK → units.id (CASCADE) |
| room | INT | NULL |
| initial_dose | DOUBLE | NULL |
| safety_threshold | DOUBLE | NULL |
| isolation_days | INT | NULL |
| start_date | DATETIME | NOT NULL |
| end_date | DATETIME | NULL |
| is_active | BOOLEAN | DEFAULT TRUE |

### 7. radiation_logs
Radiation level measurements over time.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| fk_treatment_id | INT | FK → treatments.id (SET NULL) |
| radiation_level | DOUBLE | NOT NULL |
| timestamp | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 8. health_logs
General health metrics from wearables.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| bpm | INT | NULL |
| steps | INT | NULL |
| distance | DOUBLE | NULL |
| timestamp | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 9. health_metrics
Treatment-specific health monitoring.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_treatment_id | INT | FK → treatments.id (SET NULL) |
| fk_patient_id | INT | FK → patients.id (SET NULL) |
| bpm | INT | NULL |
| steps | INT | NULL |
| distance | DOUBLE | NULL |
| current_radiation | DOUBLE | NULL |
| recorded_at | DATETIME | NULL |

### 10. doctor_alerts
Alerts generated during treatment monitoring.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| fk_treatment_id | INT | FK → treatments.id (CASCADE) |
| alert_type | VARCHAR(100) | NULL |
| message | TEXT | NULL |
| is_resolved | BOOLEAN | DEFAULT FALSE |
| created_at | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 11. motivational_messages
Encouraging messages sent to patients.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| message_text | TEXT | NOT NULL |
| is_read | BOOLEAN | DEFAULT FALSE |
| sent_at | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 12. game_sessions
Gamification data for patient engagement.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE) |
| score | INT | DEFAULT 0 |
| level_reached | INT | DEFAULT 1 |
| played_at | DATETIME | DEFAULT CURRENT_TIMESTAMP |

### 13. settings
Patient preferences and configuration.

| Column | Type | Constraints |
|--------|------|-------------|
| id | INT | PK, AUTO_INCREMENT |
| fk_patient_id | INT | FK → patients.id (CASCADE), UNIQUE |
| unit_preference | VARCHAR(50) | DEFAULT 'metric' |
| theme | VARCHAR(50) | DEFAULT 'light' |
| notifications_enabled | BOOLEAN | DEFAULT TRUE |
| updated_at | DATETIME | ON UPDATE CURRENT_TIMESTAMP |

## Sample Data Summary

- **5 Users**: 1 admin, 4 doctors
- **8 Patients**: All with active treatments
- **8 Radioisotopes**: Including I-131, Tc-99m, Y-90, Lu-177
- **8 Units**: mCi, Bq, Gy, Sv, etc.
- **8 Smartwatches**: One per patient
- **8 Active Treatments**: All patients undergoing therapy
- **Multiple Log Entries**: Radiation, health metrics, alerts

## Indexes

Primary keys are automatically indexed. Additional indexes created for:
- `user.email` (UNIQUE)
- `patients.family_access_code` (UNIQUE)
- `smartwatches.imei` (UNIQUE)
- `smartwatches.mac_address` (UNIQUE)
- `settings.fk_patient_id` (UNIQUE)

## Foreign Key Relationships

All foreign keys use `ON DELETE CASCADE` where appropriate to ensure data integrity when parent records are deleted. SET NULL is used for optional relationships (e.g., smartwatch unlinking from treatment).
