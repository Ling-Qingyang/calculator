# ==============================================================================
# Multi-Stage Dockerfile for Java Calculator Application
# Stage 1: Build & Packaging (Maven + OpenJDK 17)
# Stage 2: Minimal Production Runtime (Lightweight JRE)
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Environment
# ------------------------------------------------------------------------------
# FROM: Specifies the foundational base image containing full JDK 17 and Maven 3.8.
# The 'AS builder' naming alias enables subsequent stages to reference files built here.
FROM maven:3.8-openjdk-17 AS builder

# WORKDIR: Defines the container working directory where all subsequent commands execute.
WORKDIR /app

# COPY: Copies Maven build configuration (pom.xml) and application source tree into container.
COPY pom.xml .
COPY src ./src

# RUN: Executes Maven compilation, automated JUnit 5 testing, and creates shaded executable jar.
RUN mvn clean package -DskipTests=false

# ------------------------------------------------------------------------------
# Stage 2: Runtime Environment
# ------------------------------------------------------------------------------
# FROM: Uses an ultra-lightweight Alpine Linux JRE image to minimize image footprint and attack surface.
FROM eclipse-temurin:17-jre-alpine


# WORKDIR: Sets the application execution directory within the lightweight runtime container.
WORKDIR /app

# COPY: Transfers the compiled shaded JAR from the 'builder' stage into runtime environment.
# Build tools, compilers, and raw source files are deliberately discarded from the final image.
COPY --from=builder /app/target/calculator-1.0.jar app.jar

# ENTRYPOINT: Configures container as an executable invoking the Java Virtual Machine.
# This specifies the fixed binary entry command that receives trailing container arguments.
ENTRYPOINT ["java", "-jar", "app.jar"]

# CMD: Supplies default command-line parameters to ENTRYPOINT when no arguments are passed.
# Defaults to a demonstrative arithmetic addition ("10 + 20") producing deterministic output.
CMD ["10", "+", "20"]
