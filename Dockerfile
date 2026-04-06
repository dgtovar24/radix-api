FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar wrapper y pom.xml primero para aprovechar cache de capas
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias en capa separada (se cachea si pom.xml no cambia)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copiar código fuente y compilar
COPY src src
RUN ./mvnw clean package -DskipTests -q

# --- Runtime image ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Health check usando el actuator que ya está configurado
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
