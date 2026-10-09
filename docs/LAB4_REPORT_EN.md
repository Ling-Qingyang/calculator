# Lab 4 Technical Report: Calculator Unit Testing and JaCoCo Coverage

- **GitHub ID**: Ling-Qingyang
- **Package**: `com.lingqingyang`
- **Environment**: Java 17, Maven 3.8+
- **Test Libraries**: JUnit 5 (5.10.0), AssertJ (3.25.3), JaCoCo (0.8.11)

---

## 1. Requirements and Implementation Matrix

| Requirement | Specification | Implementation Details | Status |
| :--- | :--- | :--- | :--- |
| 1. Arithmetic Tests | Cover `+`, `-`, `*` | Tests for positive, negative, zero, decimal, and padded inputs | Implemented |
| 2. Divide by Zero | Catch zero division with `assertThrows` | JUnit `assertThrows` and AssertJ `assertThatThrownBy` | Implemented |
| 3. Parameterized Tests | 5+ test cases | 10 arithmetic cases and 4 blank input cases via `@CsvSource` | Implemented |
| 4. JaCoCo Coverage | Line coverage >= 80% | `Calculator` line coverage 100%, branch coverage 100% | Implemented |
| 5. Bug/Path Handling | Cover and fix error paths | Handles blank inputs, nulls, invalid operators, non-numeric input | Implemented |
| 6. Bonus | Use AssertJ instead of JUnit asserts | AssertJ assertions (`assertThat`, `assertThatThrownBy`) | Implemented |

---

## 2. Test Cases Overview

Test classes: `CalculatorTest.java` (29 tests), `AppTest.java` (3 tests), total 32 tests.

1. **Arithmetic Operations**:
   - `testAdd`: Positive, negative, decimal, zero, strings with leading/trailing whitespace.
   - `testSubtract`: Positive, negative, negative results, decimal numbers.
   - `testMultiply`: Positive, negative, zero, decimal numbers.
   - `testDivide` and `testPowerAndModulo`: Division, power (`^`), and modulo (`%`).

2. **Divide by Zero**:
   - Inputs `/ 0`, `/ 0.0`, `/ -0.0`, `% 0`, `% 0.0`.
   - Throws `IllegalArgumentException` with message `"division by zero"`.
   - Terminal exception output:
     ![Divide by Zero Exception](screenshots/01_division_by_zero.png)

3. **Parameterized Tests**:
   - `@ParameterizedTest` + `@CsvSource`: 10 arithmetic cases.
   - `@ParameterizedTest` + `@CsvSource`: 4 blank/whitespace operand cases.
   - `@ParameterizedTest` + `@ValueSource`: 5 unknown operator cases.

4. **Error Path Handling**:
   - Blank or null operands: Throws `IllegalArgumentException("input is blank")`.
   - Blank or null operator: Throws `IllegalArgumentException("operator is blank")`.
   - Non-numeric parsing: Catches `NumberFormatException` and throws `IllegalArgumentException`.
   - Unknown operator: Throws `IllegalArgumentException("unknown operator: ...")`.

---

## 3. JaCoCo Coverage Data

Report path: `target/site/jacoco/index.html`

| Class | Instruction Coverage | Branch Coverage | Line Coverage | Complexity | Method Coverage |
| :--- | :---: | :---: | :---: | :---: | :---: |
| `com.lingqingyang.Calculator` | 100% (98/98) | 100% (17/17) | 100% (22/22) | 100% (13/13) | 100% (2/2) |
| `com.lingqingyang.App` | 100% (31/31) | 100% (2/2) | 100% (8/8) | 100% (3/3) | 100% (2/2) |
| **Total** | **100% (129/129)** | **100% (19/19)** | **100% (30/30)** | **100% (16/16)** | **100% (4/4)** |

### Coverage Screenshots

- JaCoCo Overview:
  ![JaCoCo Overview](screenshots/03_jacoco_overview.png)

- Calculator Coverage Details:
  ![Calculator Coverage Details](screenshots/04_calculator_coverage.png)

---

## 4. Execution Commands and Outputs

1. Run test suite:
   ```bash
   mvn clean test
   ```
   Output: `Tests run: 32, Failures: 0, Errors: 0, Skipped: 0`.

   - Terminal Test Execution Screenshot:
     ![All Tests Pass Screenshot](screenshots/02_all_tests_pass.png)

2. Package and execute jar:
   ```bash
   mvn package
   java -jar target/calculator-1.0.jar 2 + 2
   ```
   Output: `4.0`.

   - Terminal CLI Packaging & Execution Screenshot:
     ![CLI Execution Output](screenshots/05_cli_execution.png)
