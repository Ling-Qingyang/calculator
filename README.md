# Java Calculator Application (Lab 6 Containerized Edition)

[![Calculator CI Pipeline](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml/badge.svg)](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml)
[![Docker Image Version](https://img.shields.io/badge/docker%20image-leon111w%2Fcalculator%3Alab6-blue?logo=docker)](https://hub.docker.com/r/leon111w/calculator)
[![Java Version](https://img.shields.io/badge/Java-17%20%7C%2021-orange?logo=openjdk)](https://adoptium.net/)

A production-grade, containerized Java CLI Calculator developed for **Software Development Tools (Lab 6)**. This project demonstrates full-lifecycle DevSecOps: comprehensive JUnit 5 unit testing, JaCoCo code coverage analysis, multi-stage Docker containerization with an ultra-lightweight Alpine JRE runtime, and an automated GitHub Actions CI/CD pipeline that compiles, tests, builds Docker images, and automatically publishes them to Docker Hub.

---

## 1. Core Capabilities & Architectural Design
- **Arithmetic Engine**: High-precision evaluation of Addition (`+`), Subtraction (`-`), Multiplication (`*`), Division (`/`), Exponentiation (`^`), and Modulo (`%`).
- **Defensive Engineering**: Robust input sanitization, non-numeric exception handling, and explicit prevention of division-by-zero (`IllegalArgumentException`).
- **Multi-Stage Docker Containerization**:
  - **Stage 1 (Builder)**: Utilizes official `maven:3.8-openjdk-17` to fetch dependencies, execute 32 JUnit 5 tests, and generate an executable shaded Fat JAR.
  - **Stage 2 (Runtime)**: Minimal `eclipse-temurin:17-jre-alpine` base image ensuring an ultra-compact footprint (~250MB), non-root execution capability, and reduced vulnerability attack surface.
- **Continuous Integration & Delivery (CI/CD)**: GitHub Actions workflow (`ci-calculator.yml`) running matrix builds across JDK 17 & 21, automatically verifying container builds and publishing tagged images to Docker Hub.

---

## 2. Technical Stack & Environment
- **Language**: Java 17 LTS (Compatible with Java 21 LTS)
- **Build System**: Apache Maven 3.8+
- **Test Framework**: JUnit Jupiter 5.10.0, AssertJ 3.25.3, JaCoCo 0.8.11
- **Container Engine**: Docker Engine 28.5.1 / Docker Desktop 4.50.0
- **Base Images**: `maven:3.8-openjdk-17` (Build stage) & `eclipse-temurin:17-jre-alpine` (Runtime stage)
- **Public Container Registry**: [Docker Hub - leon111w/calculator](https://hub.docker.com/r/leon111w/calculator)

---

## 3. Docker Usage & Execution Guide

### Pull Pre-built Image from Docker Hub
```bash
# Pull the verified Lab 6 image directly from Docker Hub
docker pull leon111w/calculator:lab6

# Run default demonstration calculation (10 + 20)
docker run --rm leon111w/calculator:lab6

# Run custom calculations
docker run --rm leon111w/calculator:lab6 15 + 27
docker run --rm leon111w/calculator:lab6 100 - 35
docker run --rm leon111w/calculator:lab6 8 * 9
docker run --rm leon111w/calculator:lab6 144 / 12
```

### Local Build & Run
```bash
# 1. Build local container image using multi-stage Dockerfile
docker build -t calculator:v1 .

# 2. Inspect built image and size
docker images | grep calculator

# 3. Execute containerized calculator
docker run --rm calculator:v1 50 / 5
```

---

## 4. Local Maven Development
```bash
# Compile and clean previous artifacts
mvn clean compile

# Execute all 32 unit tests with JaCoCo coverage analysis
mvn test

# Package standalone shaded Fat JAR
mvn package

# Run packaged executable JAR directly
java -jar target/calculator-1.0.jar 42 * 2
```

---

## 5. Automated CI/CD Workflow (`.github/workflows/ci-calculator.yml`)
1. **Triggers**: Automated on all `push` and `pull_request` events targeting `main`.
2. **Job 1 (`build-and-test`)**: Matrix validation across OpenJDK 17 and OpenJDK 21 on Ubuntu runners with Maven dependency caching.
3. **Job 2 (`docker-build`)**: Triggered upon successful testing on `main`:
   - Builds container image using Docker Buildx.
   - Tests default and parameterized arithmetic executions inside the fresh container.
   - Securely authenticates with Docker Hub via repository secrets (`DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`).
   - Pushes updated images with `:lab6` and `:latest` tags.
