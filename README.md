# CLI Calculator (Lab 3 & Lab 4)

[English](README.md) | [中文说明](README_zh.md)

A Java command-line calculator supporting basic arithmetic, power, and modulo operations, with input validation, exception handling, and 100% unit test coverage.

- **GitHub ID**: Ling-Qingyang
- **Environment**: JDK 17, Maven 3.8+
- **Dependencies**: Apache Commons Lang 3.14.0
- **Testing Libraries**: JUnit 5 (5.10.0), AssertJ (3.25.3), JaCoCo (0.8.11)

---

## Documentation Index

- [Lab 4 Technical Report (English)](docs/LAB4_REPORT_EN.md): Contains requirement matrices, test suite design, and JaCoCo coverage metrics.
- [Lab 4 实验报告 (中文)](docs/LAB4_REPORT.md): 包含需求实现对照表、测试用例分布与 JaCoCo 覆盖率统计。
- [Screenshots](docs/screenshots/): Contains test execution, error handling, and coverage screenshots.

---

## Features

- **Operators**: `+`, `-`, `*`, `/`, `^`, `%`
- **Exception Handling**:
  - Blank or null operands: Throws `IllegalArgumentException("input is blank")`
  - Blank or null operator: Throws `IllegalArgumentException("operator is blank")`
  - Non-numeric input: Catches `NumberFormatException` and throws `IllegalArgumentException`
  - Zero division or modulo by zero (`0`, `0.0`, `-0.0`): Throws `IllegalArgumentException("division by zero")`
  - Unknown operator: Throws `IllegalArgumentException("unknown operator: ...")`

---

## Getting Started

### 1. Run Tests and Generate Coverage Report
```bash
mvn clean test
```
JaCoCo HTML coverage report path: `target/site/jacoco/index.html`.

### 2. Package and Execute
```bash
mvn clean package
java -jar target/calculator-1.0.jar 2 + 2
# Output: 4.0
```

---

## Test Statistics

- **Total Test Cases**: 32 (Passed: 32, Failed: 0, Errors: 0)
- **JaCoCo Coverage**:
  - `Calculator.java`: 100% Line Coverage, 100% Branch Coverage
  - `App.java`: 100% Line Coverage, 100% Branch Coverage
- **Assertion Library**: AssertJ (`assertThat`, `assertThatThrownBy`)
