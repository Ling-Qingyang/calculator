# CLI Calculator (Lab 3 & Lab 4)

Java 命令行计算器，支持四则运算、乘方与取模，包含异常处理与单元测试。

- **GitHub ID**：Ling-Qingyang
- **环境**：JDK 17 + Maven 3.8+
- **依赖**：Apache Commons Lang 3.14.0
- **测试框架**：JUnit 5 (5.10.0) + AssertJ 3.25.3 + JaCoCo 0.8.11

---

## 功能

- **算符**：`+`, `-`, `*`, `/`, `^`, `%`
- **异常处理**：
  - 输入为空或仅包含空白字符：抛出 `IllegalArgumentException("input is blank")`
  - 操作符为空或仅包含空白字符：抛出 `IllegalArgumentException("operator is blank")`
  - 输入非数字字符串：捕获 `NumberFormatException` 并抛出 `IllegalArgumentException`
  - 除数或模数为 `0`、`0.0`、`-0.0`：抛出 `IllegalArgumentException("division by zero")`
  - 未知操作符：抛出 `IllegalArgumentException("unknown operator: ...")`

---

## 运行方式

### 1. 运行测试
```bash
mvn clean test
```
JaCoCo 报告输出路径：`target/site/jacoco/index.html`。

### 2. 打包与执行
```bash
mvn clean package
java -jar target/calculator-1.0.jar 2 + 2
```

---

## 测试统计

- **用例数**：32（通过 32，失败 0，错误 0）
- **JaCoCo 覆盖率**：
  - `Calculator.java`：行覆盖率 100%，分支覆盖率 100%
  - `App.java`：行覆盖率 100%，分支覆盖率 100%
- **断言库**：AssertJ
