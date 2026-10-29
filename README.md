# Multi-Container Calculator — Software Development Tools (Labs 3 – 7)

[English](README.md) | [中文说明](README_zh.md)

[![Calculator CI/CD Pipeline](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml/badge.svg)](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml)
[![Docker Hub Image](https://img.shields.io/badge/docker%20hub-leon111w%2Fcalculator%3Alab6-blue?logo=docker)](https://hub.docker.com/r/leon111w/calculator)
[![Java Version](https://img.shields.io/badge/Java-17%20%7C%2021-orange?logo=openjdk)](https://adoptium.net/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-v2-blue?logo=docker)](docker-compose.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An enterprise-grade, multi-container distributed Calculator application developed throughout the **Software Development Tools** course curriculum at **Waterford Institute, NUIST**. This repository showcases an end-to-end modern software engineering lifecycle: from initial modular architecture and unit testing to continuous integration (CI), containerized continuous delivery (CD), and multi-container orchestration with Docker Compose.

- **Student Author**: Ling Qingyang (ID: 202583930013)
- **Institution**: Waterford Institute, Software Engineering (Class SE 01, Grade 2025)
- **Instructor**: Prof. Yuanfang Zhang
- **Source Repository**: [https://github.com/Ling-Qingyang/calculator](https://github.com/Ling-Qingyang/calculator)

---

## 1. Laboratory Evolution & Engineering Milestones

| Lab Milestone | Focus Area | Key Architectural & Engineering Deliverables |
| :--- | :--- | :--- |
| **Lab 3: Core Architecture & CLI** | Java CLI Tooling | • Core arithmetic engine supporting `+`, `-`, `*`, `/`, `^`, `%`<br>• Defensive input sanitization and division-by-zero prevention<br>• Maven project scaffolding and Maven Shade Plugin packaging |
| **Lab 4: Automated Testing & Coverage** | Quality Assurance | • 32 comprehensive unit test cases implemented with JUnit 5 & AssertJ<br>• Boundary values and error condition verification<br>• JaCoCo code coverage integration achieving **100% line & branch coverage** |
| **Lab 5: Continuous Integration (CI)** | Cloud CI Pipeline | • GitHub Actions automated CI workflow (`ci-calculator.yml`)<br>• Test matrix validation executing across OpenJDK 17 & OpenJDK 21<br>• Automated build status badges |
| **Lab 6: Containerization & Delivery (CD)** | Docker & Cloud CD | • Multi-stage `Dockerfile` (`maven:3.8-openjdk-17` → `eclipse-temurin:17-jre-alpine`)<br>• Image footprint optimization from 1.26 GB to **257 MB**<br>• Automated container build, verification, and publish to Docker Hub |
| **Lab 7: Multi-Container Orchestration** | Docker Compose Architecture | • Directory refactoring into independent `backend/` and `web/` microservices<br>• Nginx reverse proxy routing web requests to internal service `backend:8000`<br>• `docker-compose.yml` multi-container lifecycle management & CI pipeline validation |

---

## 2. Multi-Container Architecture (Lab 7)

```text
+-------------------+              +----------------------+              +-------------------------+
|   Host Browser    |   HTTP:8080  |    Web Container     |  Docker DNS  |    Backend Container    |
| (127.0.0.1:8080)  | ===========> |   (Nginx :80 Proxy)  | ===========> | (Java REST API :8000)   |
+-------------------+              +----------------------+  http://     +-------------------------+
                                                              backend:8000
```

1. **`web` Service**: Minimal Alpine Nginx container hosting responsive static assets and reverse-proxying `/api/*` to `http://backend:8000`.
2. **`backend` Service**: High-performance Java 17 REST API microservice exposing calculation endpoints on port 8000.
3. **Bridge Network (`calc-network`)**: Dedicated bridge network providing automatic internal DNS service name resolution.

---

## 3. Quick Start with Docker Compose

### Start All Services
```bash
docker compose up -d --build
```

### Check Service Status
```bash
docker compose ps
```

### Inspect Logs
```bash
docker compose logs -f
```

### Access Application
- Web Calculator UI: [http://127.0.0.1:8080](http://127.0.0.1:8080)
- Backend Direct Health Endpoint: [http://127.0.0.1:8000/health](http://127.0.0.1:8000/health)
- Backend Direct Calculation API: `http://127.0.0.1:8000/api/calculate?a=25&op=%2A&b=4`

### Stop and Clean Up
```bash
docker compose down
```
