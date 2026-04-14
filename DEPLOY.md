# Radix API v2 - Deployment Guide

## Docker Image

The Dockerfile is at the repository root. Multi-stage build using `eclipse-temurin:21`.

## Environment Variables

Set these in your deployment platform (Dokploy):

| Variable | Value | Description |
|----------|-------|-------------|
| DB_HOST | `base-de-datos-radix-6awzza` | MySQL internal hostname |
| DB_PORT | `3306` | MySQL port |
| DB_NAME | `radixDB` | Database name |
| DB_USER | `root` | Database user |
| DB_PASSWORD | `Diegoelmejor1.0` | Database password |
| SERVER_PORT | `8080` | Application port |

**Public connection:** `mysql://root:Diegoelmejor1.0@132.145.194.97:3306/radixDB`

## Deploy Steps (Dokploy)

1. Connect Git repository
2. Select "Dockerfile" as build method
3. Set root directory to project root
4. Set environment variables (see table above)
5. Deploy
6. Configure domain `api.raddix.pro` → container port 8080

## Build (Local)

```bash
./mvnw clean package -DskipTests
docker build -t radix-api:v2 .
```

## Verify Deployment

```bash
curl https://api.raddix.pro/actuator/health
# Expected: {"status":"UP"}
```
