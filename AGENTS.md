# AGENTS.md

## Dev Commands

```bash
./mvnw spring-boot:run              # local dev (H2 in-memory, no setup)
./mvnw test                         # all tests
./mvnw test -Dtest=ClassName        # single test class
./mvnw clean package -DskipTests     # JAR build
```

## v1 Endpoints (Main branch)

Base path: `/api`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/login` | Login with email/password |
| POST | `/api/auth/register` | Register new user |
| GET | `/actuator/health` | Health check (Spring Boot Actuator) |

## What's Wired vs. What's Scaffold

- `Usuario` entity → `UsuarioRepository` → `AuthController` → `/api/auth`
- `Facultativo`, `Paciente`, `Tratamiento`, `MetricasSalud`, etc. exist as JPA entities but have **no controllers or services**

## Database

- Local: H2 in-memory (`jdbc:h2:mem:radixdb`), schema auto-created from entities via `ddl-auto: update`
- Production: MySQL via env vars `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`; activated by `-Dspring.profiles.active=prod`
- **Default MySQL credentials are hardcoded in `application-prod.yml`** — do not commit real secrets

## Tech Stack

- Spring Boot 3.5.9, Java 21, Maven
- Spring Boot Actuator for health/metrics
- JPA/Hibernate with `ddl-auto: update` — entities are the schema source of truth

## Deployment

- Dockerfile uses multi-stage build (`eclipse-temurin:21`), sets `spring.profiles.active=prod` at runtime
- Built and deployed on Dokploy; see `DEPLOY.md` for env var configuration
- API accessible at `https://api.raddix.pro/v1`