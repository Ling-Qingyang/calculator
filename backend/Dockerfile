# ==============================================================================
# Multi-Stage Dockerfile for Calculator Backend Microservice
# Service: backend
# Exposed Port: 8000
# Base Environment: Maven 3.8 + OpenJDK 17 (Builder) -> Eclipse Temurin 17 JRE (Alpine)
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build & Compilation Stage
# ------------------------------------------------------------------------------
# FROM: Use standard official Maven with OpenJDK 17 as build environment
FROM maven:3.8-openjdk-17 AS builder

# WORKDIR: Set the dedicated workspace inside the build container
WORKDIR /app

# COPY: Copy build configuration (pom.xml) first for optimal layer caching
COPY pom.xml .

# COPY: Copy application source code into builder container
COPY src ./src

# RUN: Compile source, execute all unit tests, and package standalone executable jar
RUN mvn clean package -DskipTests=false

# ------------------------------------------------------------------------------
# Stage 2: Minimal Production Runtime Stage
# ------------------------------------------------------------------------------
# FROM: Ultra-lightweight Alpine Linux Adoptium OpenJDK 17 JRE image (~140MB)
FROM eclipse-temurin:17-jre-alpine

# LABEL: Provide service metadata for container management
LABEL maintainer="Ling Qingyang <279034363+Ling-Qingyang@users.noreply.github.com>"
LABEL service="calculator-backend"
LABEL course="Software Development Tools - Lab 7"

# WORKDIR: Define runtime directory
WORKDIR /app

# COPY: Transfer shaded executable jar from builder stage, discarding build tools & source
COPY --from=builder /app/target/calculator-1.0.jar app.jar

# ENV: Configure default environment variables for server execution
ENV PORT=8000 \
    LOG_LEVEL=INFO \
    APP_ENV=production

# EXPOSE: Declare container listens on port 8000 for backend HTTP REST API
EXPOSE 8000

# ENTRYPOINT: Launch Java Virtual Machine with optimized container memory flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

# CMD: Pass default argument to start HTTP server mode
CMD ["server"]
