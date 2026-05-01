# AGENTS.md

## Dev Commands

```bash
./mvnw spring-boot:run              # local dev (H2 in-memory, no setup)
./mvnw test                         # all tests (14 tests, 3 skipped)
./mvnw test -Dtest=ClassName        # single test class
./mvnw clean package -DskipTests    # JAR build
```

## Endpoints (66 REST + 3 WebSocket)

Base path: `/v2` (local) / `/v1` (production via `CONTEXT_PATH` env var).

| Controller | Endpoints | Key Paths |
|------------|-----------|-----------|
| `AuthController` | 4 | `/api/auth/login`, `/api/auth/register/doctor`, `/api/auth/register/patient`, `/api/auth/token` |
| `PatientController` | 6 | `/api/patients`, `/api/patients/{id}`, `/api/patients/register` |
| `UserController` | 5 | `/api/users`, `/api/users/{id}`, `/api/users/role/{role}` |
| `DoctorController` | 3 | `/api/doctors`, `/api/doctors/{id}` |
| `SmartwatchController` | 6 | `/api/smartwatches`, `/api/smartwatches/patient/{id}` |
| `TreatmentController` | 6 | `/api/treatments`, `/api/treatments/patient/{id}` |
| `DoctorAlertController` | 4 | `/api/alerts`, `/api/alerts/pending` |
| `WatchDataController` | 3 | `/api/watch/ingest`, `/api/watch/{imei}/metrics` |
| `HealthMetricsController` | 7 | `/api/health-metrics`, `/api/radiation-logs`, `/api/health-logs` |
| `MessageController` | 3 | `/api/messages/patient/{id}` |
| `GameController` | 2 | `/api/games/patient/{id}` |
| `SettingsController` | 2 | `/api/settings/patient/{id}` |
| `UnitController` | 2 | `/api/units` |
| `OAuthClientController` | 2 | `/api/oauth-clients` |
| `IsotopeController` | 2 | `/api/isotopes` |
| `DashboardController` | 1 | `/api/dashboard/stats` |
| `FileController` | 2 | `/api/upload`, `/api/files/{filename}` |
| `HealthController` | 1 | `/` (health check) |
| `DocsController` | 1 | `/docs` (API schema) |
| WebSocket | 3 | `/ws/alerts`, `/ws/chat`, `/ws/rix` |

## Auth

- **JWT (HS384)** with 24h expiry. Claims: `sub` (userId), `role`, `firstName`, `iat`, `exp`.
- **BCrypt** password hashing via `spring-security-crypto`.
- No Spring Security auto-config (only `PasswordEncoder` bean in `SecurityConfig.java`).
- `JwtUtil.java` in `src/main/java/com/project/radix/Util/` handles token generation/validation.
- Tokens are extracted from `Authorization: Bearer <token>` header, validated via `jwtUtil.getUserId(token)`.

## Database

- **Local:** H2 in-memory (`jdbc:h2:mem:radixdb`), schema via `ddl-auto: update`
- **Production:** MySQL via env vars `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`; activated by `-Dspring.profiles.active=prod`
- **Seed data:** `DataLoader.java` runs on startup if `userRepo.count() == 0`. Creates admin + 6 doctors + 12 patients + treatments + alerts + health data.
- 14 JPA entities, all with at least one REST endpoint.
- `spring.sql.init.mode=never` — schema.sql is not executed.

## Tech Stack

- Spring Boot 3.5.9, Java 21, Maven
- JWT via `io.jsonwebtoken:jjwt-api:0.12.6`
- BCrypt via `spring-security-crypto`
- File uploads via `FileController` (multipart, 50MB limit, stored in `/home/ubuntu/radix-uploads/` volume)
- WebSocket via `spring-boot-starter-websocket`
- Actuator for health/metrics

## Deployment

- Dockerfile: multi-stage `eclipse-temurin:21`, sets `spring.profiles.active=prod`
- Deployed on Dokploy via GitHub Actions self-hosted runner
- Traefik routes `api.raddix.pro` and `api.aiflex.dev` to container port 9000
- Volume mount: `/home/ubuntu/radix-uploads:/home/ubuntu/radix-uploads`
- `application-prod.yml` context-path: `/v1`

## File Uploads

- `POST /v1/api/upload` — multipart, 50MB limit
- `GET /v1/api/files/{filename}` — auto Content-Type
- Files stored on Docker volume for persistence across container restarts
