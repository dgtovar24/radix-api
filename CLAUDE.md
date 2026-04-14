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
- **H2** in-memory DB for local dev; **MySQL** for production (activated via `spring.profiles.active=prod`)
- JPA/Hibernate with `ddl-auto=update` — schema is auto-managed from entity classes

### Package Structure

| Package | Purpose |
|---|---|
| `Controller/` | REST controllers — `AuthController` at `/api/auth` |
| `Model/` | JPA entities mapping to MySQL schema |
| `Repository/` | Spring Data JPA repositories, one per entity |

### Active Endpoints

**Authentication** (`/api/auth`):
- `POST /api/auth/login` — Login with email/password
- `POST /api/auth/register` — Register new user (default rol: Doctor)

**Health**:
- `GET /actuator/health` — Application health status

### Model Entities
Most model entities (`Tratamiento`, `MetricasSalud`, `AlertaMetge`, `DispositivoReloj`, etc.) mirror the shared MySQL schema but have **no controllers or services yet**.

### Database Configuration
- **Local**: H2 in-memory (`jdbc:h2:mem:radixdb`) — no setup required, schema auto-created
- **Production**: MySQL via env vars `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` — see `application-prod.yml`; activated by `-Dspring.profiles.active=prod` (set in `Dockerfile`)

### Deployment
Docker multi-stage build (`Dockerfile`) using `eclipse-temurin:21`. Deployed on Dokploy — see `DEPLOY.md` for env var configuration.
