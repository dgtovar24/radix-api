# Radix API - Dokploy Deployment Configuration

## Docker Image
The Dockerfile is ready at `api/Dockerfile`

## Environment Variables (set in Dokploy dashboard)
```
DB_HOST=base-de-datos-radix-6awzza
DB_PORT=3306
DB_NAME=radixDB
DB_USER=root
DB_PASSWORD=Diegoelmejor1.0
SERVER_PORT=8080
```

## MySQL Database Setup
Before deploying, ensure MySQL database exists:
```sql
CREATE DATABASE radixDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Build
The Dockerfile will:
1. Build with Java 21 (eclipse-temurin)
2. Use multi-stage build for small final image
3. Expose port 8080

## Deploy Steps in Dokploy
1. Connect Git repository
2. Select "Dockerfile" as build method
3. Set root directory to `api/`
4. Set environment variables
5. Deploy