# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run locally (H2 in-memory DB, no setup required)
./mvnw spring-boot:run

# Build JAR
./mvnw clean package

# Build and skip tests
./mvnw clean package -DskipTests

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=RadixApplicationTests

# Run with production profile (MySQL)
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Architecture

### Tech Stack
- **Spring Boot 3.5.9**, Java 21, Maven
- **HAPI FHIR R4** (7.0.1) — standard HL7 FHIR patient resource handling
- **H2** in-memory DB for local dev; **MySQL** for production (activated via `spring.profiles.active=prod`)
- JPA/Hibernate with `ddl-auto=update` — schema is auto-managed from entity classes

### Package Structure

| Package | Purpose |
|---|---|
| `Controller/` | REST controllers — currently only `FHIRPatientController` at `/fhir/Patient` |
| `Service/` | Active services — `FHIRPatientService` (FHIR CRUD over `Paciente` entity) |
| `Services/` | Empty scaffold stubs — `PacientesService`, `TratamientoService` (not yet implemented) |
| `FHIR/` | `PacienteToFhirConverter` — bidirectional conversion between `Paciente` JPA entity and FHIR `Patient` resource |
| `Model/` | JPA entities mapping to the shared MySQL/Supabase schema |
| `Repository/` | Spring Data JPA repositories, one per entity |
| `Config/` | `FHIRWebConfig` — registers `application/fhir+json` as a supported media type alongside `application/json` |
| `DTO/` | `AuthRequest` / `AuthResponse` — DTOs for a future auth endpoint (not yet wired) |

### Only Active Endpoint
`/fhir/Patient` — full CRUD (GET, POST, PUT, DELETE). Accepts and returns standard FHIR R4 `Patient` JSON. The controller delegates to `FHIRPatientService`, which persists via `PacienteRepository` and converts via `PacienteToFhirConverter`.

### Model Entities vs. Active Code
Most model entities (`Tratamiento`, `MetricasSalud`, `AlertaMetge`, `DispositivoReloj`, etc.) mirror the shared Supabase schema used by the frontend but have **no controllers or services yet**. Only `Paciente` is fully wired end-to-end.

### Database Configuration
- **Local**: H2 in-memory (`jdbc:h2:mem:radixdb`) — no setup required, schema auto-created
- **Production**: MySQL via env vars `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` — see `application-prod.yml`; activated by `-Dspring.profiles.active=prod` (set in `Dockerfile`)

### Deployment
Docker multi-stage build (`Dockerfile`) using `eclipse-temurin:21`. Deployed on Dokploy — see `DEPLOY.md` for env var configuration.
