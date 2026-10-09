# CLI Calculator (Lab 3 & Lab 4)

[English](#english) | [中文说明](#中文说明)

---

## English

A Java command-line calculator supporting basic arithmetic, power, and modulo operations, with input validation, exception handling, and 100% unit test coverage.

- **GitHub ID**: Ling-Qingyang
- **Environment**: JDK 17, Maven 3.8+
- **Dependencies**: Apache Commons Lang 3.14.0
- **Testing Libraries**: JUnit 5 (5.10.0), AssertJ (3.25.3), JaCoCo (0.8.11)

### Documentation Index

- [Lab 4 Technical Report (English)](docs/LAB4_REPORT_EN.md): Contains requirement matrices, test suite design, and JaCoCo coverage metrics.
- [Lab 4 实验报告 (中文)](docs/LAB4_REPORT.md): 包含需求实现对照表、测试用例分布与 JaCoCo 覆盖率统计。

### Features

- **Operators**: `+`, `-`, `*`, `/`, `^`, `%`
- **Exception Handling**:
  - Blank or null operands: Throws `IllegalArgumentException("input is blank")`
  - Blank or null operator: Throws `IllegalArgumentException("operator is blank")`
  - Non-numeric input: Catches `NumberFormatException` and throws `IllegalArgumentException`
  - Zero division or modulo by zero (`0`, `0.0`, `-0.0`): Throws `IllegalArgumentException("division by zero")`
  - Unknown operator: Throws `IllegalArgumentException("unknown operator: ...")`

### Getting Started

#### 1. Run Tests and Generate Coverage Report
```bash
mvn clean test
```
JaCoCo HTML coverage report path: `target/site/jacoco/index.html`.

#### 2. Package and Execute
```bash
mvn clean package
java -jar target/calculator-1.0.jar 2 + 2
# Output: 4.0
```

### Test Statistics

- **Total Test Cases**: 32 (Passed: 32, Failed: 0, Errors: 0)
- **JaCoCo Coverage**:
  - `Calculator.java`: 100% Line Coverage, 100% Branch Coverage
  - `App.java`: 100% Line Coverage, 100% Branch Coverage
- **Assertion Library**: AssertJ (`assertThat`, `assertThatThrownBy`)

---

## 中文说明

Java 命令行计算器，支持四则运算、乘方与取模，包含异常处理与单元测试。

- **GitHub ID**：Ling-Qingyang
- **运行环境**：JDK 17, Maven 3.8+
- **核心依赖**：Apache Commons Lang 3.14.0
- **测试框架**：JUnit 5 (5.10.0), AssertJ (3.25.3), JaCoCo (0.8.11)

### 报告与文档入口

- [实验 4 实验报告（中文）](docs/LAB4_REPORT.md)：详细列出 5 项任务与加分项对照、32 个用例分布及 JaCoCo 统计表。
- [Lab 4 Technical Report (English)](docs/LAB4_REPORT_EN.md)：英文技术报告。

### 功能

- **支持算符**：`+`, `-`, `*`, `/`, `^`, `%`
- **异常处理**：
  - 输入为空或纯空格：抛出 `IllegalArgumentException("input is blank")`
  - 操作符为空或纯空格：抛出 `IllegalArgumentException("operator is blank")`
  - 非数字字符串：捕获 `NumberFormatException` 并抛出 `IllegalArgumentException`
  - 除数或模数为 `0`、`0.0`、`-0.0`：抛出 `IllegalArgumentException("division by zero")`
  - 未知操作符：抛出 `IllegalArgumentException("unknown operator: ...")`

### 运行方式

#### 1. 执行测试
```bash
mvn clean test
```
JaCoCo 报告输出路径：`target/site/jacoco/index.html`。

#### 2. 构建与运行
```bash
mvn clean package
java -jar target/calculator-1.0.jar 2 + 2
# 输出: 4.0
```

### 测试统计

- **用例数**：32（通过 32，失败 0，错误 0）
- **JaCoCo 覆盖率**：
  - `Calculator.java`：行覆盖率 100%，分支覆盖率 100%
  - `App.java`：行覆盖率 100%，分支覆盖率 100%
- **断言库**：AssertJ
