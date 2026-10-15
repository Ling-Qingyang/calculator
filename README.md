# Java Calculator Application

[![Calculator CI Pipeline](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml/badge.svg)](https://github.com/Ling-Qingyang/calculator/actions/workflows/ci-calculator.yml)

A modular, verified Java CLI Calculator with robust error handling, automated Maven build automation, JUnit 5 unit test suites, JaCoCo code coverage, and continuous integration via GitHub Actions.

---

## 1. Features
- **Core Operations**: Addition (`+`), Subtraction (`-`), Multiplication (`*`), Division (`/`), Exponentiation (`^`), and Modulo (`%`).
- **Defensive Input Handling**: Robust validation against blank inputs, non-numeric strings, and explicit division-by-zero prevention (`IllegalArgumentException`).
- **Continuous Integration**: Automated GitHub Actions pipeline (`ci-calculator.yml`) validating builds against JDK 17 and JDK 21 matrices on every commit and pull request.

---

## 2. Requirements & Environment
- **Java**: JDK 17+ (JDK 17 LTS or JDK 21 LTS recommended)
- **Build Tool**: Apache Maven 3.8+
- **Test Framework**: JUnit Jupiter 5.10.0 & AssertJ 3.25.3

---

## 3. Local Build & Test Commands

```bash
# Clean previous build artifacts and compile
mvn clean compile

# Execute all JUnit 5 test cases and generate JaCoCo coverage reports
mvn test

# Package standalone shaded executable JAR
mvn package

# Run the packaged executable
java -jar target/calculator-1.0.jar 15 / 3
```

---

## 4. Continuous Integration Pipeline
The project utilizes GitHub Actions to enforce continuous quality gates:
1. **Trigger Events**: `push` and `pull_request` against `main`.
2. **Matrix Build**: Parallel execution on Ubuntu runners with OpenJDK 17 and OpenJDK 21.
3. **Caching**: Automated Maven `.m2` repository caching for fast build execution.
4. **Automated Verification**: Automatic execution of `mvn clean compile` and `mvn test`.
