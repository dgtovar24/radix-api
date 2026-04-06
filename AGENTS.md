# AGENTS.md - Radix Project

## Project Overview
This is a Spring Boot 3.5.9 Java 25 application with Maven. It provides a medical/healthcare API (FHIR-compatible) with patient management, authentication, and treatment tracking.

## Build Commands

### Maven Wrapper (recommended)
```bash
./mvnw <goal>                    # Linux/macOS
./mvnw.cmd <goal>                # Windows
```

### Common Goals
```bash
./mvnw clean                     # Clean build artifacts
./mvnw compile                   # Compile source code
./mvnw test                      # Run all tests
./mvnw package                   # Build JAR file
./mvnw spring-boot:run          # Run application
./mvnw install                   # Install to local repo
```

### Single Test Execution
```bash
./mvnw test -Dtest=RadixApplicationTests              # Single test class
./mvnw test -Dtest=RadixApplicationTests#contextLoads  # Single test method
./mvnw test -Dtest="com.proyecto.radix.**"            # All tests in package
```

### Useful Options
```bash
./mvnw test -DskipTests=false    # Force run tests
./mvnw test -DfailIfNoTests=false # Don't fail if no tests found
./mvnw -q                        # Quiet output
./mvnw -X                        # Debug mode
```

## Code Style Guidelines

### Package Structure
```
com.proyecto.radix/
├── Controller/     # REST endpoints (@RestController)
├── Service/         # Business logic (@Service)
├── Repository/      # Data access (extends JpaRepository)
├── Model/          # JPA entities (@Entity)
├── DTO/             # Data Transfer Objects
└── RadixApplication.java
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `PacientesController`, `UsuarioRepository`)
- **Methods/Fields**: camelCase (e.g., `idPaciente`, `findByEmail`)
- **Packages**: lowercase with camelCase for multi-word (e.g., `com.proyecto.radix`)
- **Database columns**: snake_case (e.g., `id_paciente`, `nombre_completo`)
- **REST endpoints**: lowercase plural (e.g., `/api/pacientes`, `/api/auth`)

### Annotations
- **Entity classes**: Use Lombok `@Data` and `@NoArgsConstructor`
- **Controllers**: Use `@RestController`, `@RequestMapping`, `@RequiredArgsConstructor`, `@CrossOrigin(origins = "*")`
- **Repositories**: Extend `JpaRepository<Entity, Integer>` with `@Repository`

### JPA Entity Patterns
```java
@Entity
@Table(name = "table_name")
@Data
@NoArgsConstructor
public class EntityName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_name")
    private Integer idColumn;

    @Column(name = "column_name", nullable = false)
    private String fieldName;

    @ManyToOne
    @JoinColumn(name = "fk_column")
    private RelatedEntity relation;
}
```

### REST Controller Patterns
```java
@RestController
@RequestMapping("/api/resource")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResourceController {
    private final RepositoryName repository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getResource(@PathVariable Integer id) {
        return repository.findById(id)
                .map(r -> ResponseEntity.ok(r))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Not found")));
    }
}
```

### Error Handling
- Return `ResponseEntity.status(code).body(Map.of("error", "message"))` for errors
- HTTP 400: Bad request / validation errors
- HTTP 401: Unauthorized / invalid credentials
- HTTP 404: Resource not found
- HTTP 500: Server errors

### Import Organization
Standard order (IDE can enforce):
1. `java.*` packages
2. `javax.*` packages
3. Third-party (`org.springframework.*`, `lombok.*`, etc.)
4. Internal (`com.proyecto.radix.*`)

### Lombok Usage
- `@Data`: Generates getters, setters, toString, equals, hashCode
- `@NoArgsConstructor`: Generates no-arg constructor
- `@RequiredArgsConstructor`: Generates constructor with final fields (for dependency injection)
- Avoid `@AllArgsConstructor` unless needed

### Testing
- Use JUnit 5 (`org.junit.jupiter.api.Test`)
- Use `@SpringBootTest` for integration tests
- Test class name: `<ClassName>Test.java`
- Test method name: `<methodName>_<expectedBehavior>`

### Database
- Hibernate DDL auto: `none` (schema managed separately)
- MySQL dialect configured
- Connection in `application.properties`

### General Rules
- No comments unless explaining complex business logic
- Keep classes focused (single responsibility)
- Use dependency injection via constructor
- Return `ResponseEntity<?>` for REST endpoints
- Use `Map.of()` for simple response objects
