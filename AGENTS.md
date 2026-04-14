# AGENTS.md - Radix API

## Build Commands
```bash
./mvnw clean compile          # Compile
./mvnw test                   # Run tests
./mvnw package -DskipTests    # Build JAR
```

Local Java 17, but Docker image uses Java 21 (eclipse-temurin). Build succeeds in Docker.

## Project Structure

**Two branches with divergent histories:**
- `main` - v1, no context path
- `v2` - uses `/v2` context path prefix on all endpoints

**Package:** `com.proyecto.radix/`
- Controller/ - REST endpoints
- Model/ - JPA entities
- Repository/ - JpaRepository interfaces
- DTO/ - Data Transfer Objects
- Services/ - Business logic (v2 branch)

## Key Conventions

**Field naming (v2):** Use English field names matching database schema:
- `firstName`, `lastName`, `role`, `password`, `createdAt`
- NOT `nombre`, `apellido`, `rol`, `contrasena`, `fechaCreacion`

**Entity models:** Use Lombok `@Data` + `@NoArgsConstructor`, manually write getters/setters for `Usuario`.

**REST endpoints:** Lowercase plural, no Spanish names. Example: `@RequestMapping("/api/pacientes")`

**Error responses:** English only: `"error"`, `"Invalid credentials"`, `"Email already exists"`, `"Not found"`

## Deployment (Dokploy)

- Container port: 8080 (internal), expose via host port mapping
- Profile: `prod` with `application-prod.yml`
- Context path `/v2` configured in `application-prod.yml`: `server.servlet.context-path: /v2`
- Health endpoint: `GET /v2/` returns `{"status": "UP", "service": "radix-api", "version": "2"}`

## Database

- MySQL 8.4.8 on external host
- Connection via environment variables: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- Default: `base-de-datos-radix-6awzza:3306/radixDB`
- Hibernate DDL auto: `update`

## Testing

```bash
./mvnw test -Dtest=RadixApplicationTests              # Single class
./mvnw test -Dtest=RadixApplicationTests#contextLoads # Single method
```