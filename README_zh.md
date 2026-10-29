# 多容器计算器应用 — 软件开发工具 (实验 3 – 7)

[English](README.md) | [中文说明](README_zh.md)

[![Calculator CI/CD Pipeline](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml/badge.svg)](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml)
[![Docker Hub Image](https://img.shields.io/badge/docker%20hub-leon111w%2Fcalculator%3Alab6-blue?logo=docker)](https://hub.docker.com/r/leon111w/calculator)
[![Java Version](https://img.shields.io/badge/Java-17%20%7C%2021-orange?logo=openjdk)](https://adoptium.net/)
[![Docker Compose](https://img.shields.io/badge/Docker%20Compose-v2-blue?logo=docker)](docker-compose.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

本项目为南京信息工程大学沃特福德学院软件工程专业**软件开发工具**课程系列实验项目（实验 3 至实验 7）。项目展示了从零构建模块化 Java 计算器、自动化测试与代码覆盖率分析、GitHub Actions CI/CD 流水线，到 Docker 容器化与 Docker Compose 多容器服务编排的完整软件工程全生命周期。

- **学生姓名**：凌青阳 (学号: 202583930013)
- **培养学院**：沃特福德学院 软件工程专业 (2025级 01班)
- **指导教师**：张远方 教授
- **项目仓库**：[https://github.com/Ling-Qingyang/calculator](https://github.com/Ling-Qingyang/calculator)

---

## 1. 实验演进与工程里程碑

| 实验阶段 | 核心主题 | 架构演进与关键交付物 |
| :--- | :--- | :--- |
| **Lab 3: 核心架构与 CLI** | Java CLI 工具构建 | • 核心算术引擎，支持 `+`, `-`, `*`, `/`, `^`, `%`<br>• 防御性输入校验与除零异常处理<br>• Maven 项目脚手架与 Maven Shade 插件打包 |
| **Lab 4: 单元测试与覆盖率** | 质量保障工程 | • 基于 JUnit 5 与 AssertJ 编写的 32 个自动化测试用例<br>• 覆盖边界条件、异常输入与分支逻辑<br>• JaCoCo 覆盖率达 **100% 行与分支覆盖** |
| **Lab 5: 持续集成 (CI)** | 云端自动化流水线 | • GitHub Actions CI 流水线 (`ci-calculator.yml`)<br>• OpenJDK 17 与 OpenJDK 21 多版本矩阵并行测试<br>• 动态状态徽章集成 |
| **Lab 6: 容器化交付 (CD)** | Docker 容器化 | • 多阶段构建 `Dockerfile` (`maven:3.8` → `temurin:17-jre-alpine`)<br>• 镜像体积优化自 1.26 GB 降至 **257 MB** (缩减约 80%)<br>• 自动化构建并发布镜像至 Docker Hub |
| **Lab 7: 多容器服务编排** | Docker Compose 架构 | • 重构为前端 `web/` 与后端 `backend/` 独立微服务<br>• Nginx 反向代理将请求转发至内部服务 `backend:8000`<br>• `docker-compose.yml` 多容器生命周期管理与 Actions CI 构建验证 |

---

## 2. 多容器架构图 (Lab 7)

```text
+-------------------+              +----------------------+              +-------------------------+
|    宿主机浏览器    |   HTTP:8080  |      Web 容器        |  Docker DNS  |       后端计算容器       |
| (127.0.0.1:8080)  | ===========> |   (Nginx :80 代理)   | ===========> | (Java REST API :8000)   |
+-------------------+              +----------------------+  http://     +-------------------------+
                                                              backend:8000
```

1. **`web` 服务**：基于 Nginx Alpine 镜像，提供响应式纯黑白计算器前端网页，并将 `/api/*` 反向代理至 `http://backend:8000`。
2. **`backend` 服务**：基于 Java 17 的轻量级 REST API 微服务，8000 端口提供高吞吐算术计算服务与健康检查接口。
3. **桥接网络 (`calc-network`)**：独立的 Docker Compose 网络，提供容器间基于服务名（Service Name）的动态域名解析。

---

## 3. Docker Compose 快速上手

### 启动全部微服务
```bash
docker compose up -d --build
```

### 查看容器运行状态
```bash
docker compose ps
```

### 查看实时容器日志
```bash
docker compose logs -f
```

### 访问服务接口
- 计算器前端页面: [http://127.0.0.1:8080](http://127.0.0.1:8080)
- 后端直连健康检查: [http://127.0.0.1:8000/health](http://127.0.0.1:8000/health)
- 后端直连计算接口: `http://127.0.0.1:8000/api/calculate?a=25&op=%2A&b=4`

### 停止并清理容器
```bash
docker compose down
```
