# CLI Calculator (Lab 3 & Lab 4)

[English](README.md) | [中文说明](README_zh.md)

Java 命令行计算器，支持四则运算、乘方与取模，包含异常处理与单元测试。

- **GitHub ID**：Ling-Qingyang
- **运行环境**：JDK 17, Maven 3.8+
- **核心依赖**：Apache Commons Lang 3.14.0
- **测试框架**：JUnit 5 (5.10.0), AssertJ (3.25.3), JaCoCo (0.8.11)

---

## 报告与文档入口

- [实验 4 实验报告（中文）](docs/LAB4_REPORT.md)：包含需求实现对照表、测试用例分布与 JaCoCo 覆盖率统计。
- [Lab 4 Technical Report (English)](docs/LAB4_REPORT_EN.md)：英文技术报告。
- [实验过程截图](docs/screenshots/)：包含测试运行、异常抛出与 JaCoCo 覆盖率页面截图。

---

## 功能

- **支持算符**：`+`, `-`, `*`, `/`, `^`, `%`
- **异常处理**：
  - 输入为空或纯空格：抛出 `IllegalArgumentException("input is blank")`
  - 操作符为空或纯空格：抛出 `IllegalArgumentException("operator is blank")`
  - 非数字字符串：捕获 `NumberFormatException` 并抛出 `IllegalArgumentException`
  - 除数或模数为 `0`、`0.0`、`-0.0`：抛出 `IllegalArgumentException("division by zero")`
  - 未知操作符：抛出 `IllegalArgumentException("unknown operator: ...")`

---

## 运行方式

### 1. 执行测试
```bash
mvn clean test
```
JaCoCo 报告输出路径：`target/site/jacoco/index.html`。

### 2. 构建与运行
```bash
mvn clean package
java -jar target/calculator-1.0.jar 2 + 2
# 输出: 4.0
```

---

## 测试统计

- **用例数**：32（通过 32，失败 0，错误 0）
- **JaCoCo 覆盖率**：
  - `Calculator.java`：行覆盖率 100%，分支覆盖率 100%
  - `App.java`：行覆盖率 100%，分支覆盖率 100%
- **断言库**：AssertJ
