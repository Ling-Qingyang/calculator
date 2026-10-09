package com.lingqingyang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class CalculatorTest {

    private final Calculator c = new Calculator();

    // ==========================================
    // Task 1: Tests for add, subtract, multiply
    // ==========================================

    @Test
    @DisplayName("Test addition with positive, negative, decimal numbers and whitespace")
    void testAdd() {
        assertThat(c.eval("2", "+", "3")).isEqualTo(5.0);
        assertThat(c.eval("-4", "+", "-6")).isEqualTo(-10.0);
        assertThat(c.eval("-5", "+", "8")).isEqualTo(3.0);
        assertThat(c.eval("1.5", "+", "2.25")).isEqualTo(3.75);
        assertThat(c.eval("0", "+", "100")).isEqualTo(100.0);
        assertThat(c.eval("  12  ", "+", "  8  ")).isEqualTo(20.0);
    }

    @Test
    @DisplayName("Test subtraction with positive, negative and decimal numbers")
    void testSubtract() {
        assertThat(c.eval("5", "-", "2")).isEqualTo(3.0);
        assertThat(c.eval("2", "-", "5")).isEqualTo(-3.0);
        assertThat(c.eval("-10", "-", "-4")).isEqualTo(-6.0);
        assertThat(c.eval("5.5", "-", "2.2")).isCloseTo(3.3, within(1e-9));
        assertThat(c.eval("0", "-", "7")).isEqualTo(-7.0);
    }

    @Test
    @DisplayName("Test multiplication with positive, negative, zero and decimal numbers")
    void testMultiply() {
        assertThat(c.eval("3", "*", "5")).isEqualTo(15.0);
        assertThat(c.eval("99", "*", "0")).isEqualTo(0.0);
        assertThat(c.eval("-3", "*", "4")).isEqualTo(-12.0);
        assertThat(c.eval("-2", "*", "-8")).isEqualTo(16.0);
        assertThat(c.eval("2.5", "*", "4")).isEqualTo(10.0);
    }

    @Test
    @DisplayName("Test valid division")
    void testDivide() {
        assertThat(c.eval("10", "/", "5")).isEqualTo(2.0);
        assertThat(c.eval("-15", "/", "3")).isEqualTo(-5.0);
        assertThat(c.eval("7.5", "/", "2.5")).isEqualTo(3.0);
    }

    @Test
    @DisplayName("Test power and modulo operators")
    void testPowerAndModulo() {
        assertThat(c.eval("2", "^", "3")).isEqualTo(8.0);
        assertThat(c.eval("5", "^", "0")).isEqualTo(1.0);
        assertThat(c.eval("7", "%", "3")).isEqualTo(1.0);
        assertThat(c.eval("10", "%", "5")).isEqualTo(0.0);
    }

    // ==========================================
    // Task 2: Test divide by zero (assertThrows & AssertJ)
    // ==========================================

    @Test
    @DisplayName("Test divide by zero using JUnit assertThrows")
    void testDivideByZeroWithAssertThrows() {
        assertThrows(IllegalArgumentException.class, () -> c.eval("10", "/", "0"));
        assertThrows(IllegalArgumentException.class, () -> c.eval("10", "/", "0.0"));
        assertThrows(IllegalArgumentException.class, () -> c.eval("10", "/", "-0.0"));
        assertThrows(IllegalArgumentException.class, () -> c.eval("10", "%", "0"));
        assertThrows(IllegalArgumentException.class, () -> c.eval("10", "%", "0.0"));
    }

    @Test
    @DisplayName("Test divide by zero using AssertJ fluent assertion (Bonus)")
    void testDivideByZeroWithAssertJ() {
        assertThatThrownBy(() -> c.eval("42", "/", "0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("division by zero");

        assertThatThrownBy(() -> c.eval("42", "/", "-0.0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("division by zero");

        assertThatThrownBy(() -> c.eval("42", "%", "0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("division by zero");
    }

    // ==========================================
    // Task 3: Parameterized tests with 5+ cases
    // ==========================================

    @ParameterizedTest(name = "[{index}] {0} {1} {2} = {3}")
    @CsvSource({
            "1, +, 1, 2.0",
            "10, -, 4, 6.0",
            "3, *, 7, 21.0",
            "20, /, 4, 5.0",
            "2, ^, 8, 256.0",
            "10, %, 3, 1.0",
            "-5, +, -3, -8.0",
            "-6, *, 7, -42.0",
            "2.5, *, 4, 10.0",
            "100, -, 0.5, 99.5"
    })
    @DisplayName("Parameterized test with 10 calculation test cases")
    void testParameterizedCalculations(String a, String op, String b, double expected) {
        assertThat(c.eval(a, op, b)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "'', +, 2",
            "'   ', +, 2",
            "2, +, ''",
            "2, +, '   '"
    })
    @DisplayName("Parameterized test for blank operand inputs")
    void testParameterizedBlankOperands(String a, String op, String b) {
        assertThatThrownBy(() -> c.eval(a, op, b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("input is blank");
    }

    // ==========================================
    // Additional Error Path & Edge Case Coverage
    // ==========================================

    @Test
    @DisplayName("Test blank or null operator throws IllegalArgumentException")
    void testBlankOperatorThrows() {
        assertThatThrownBy(() -> c.eval("1", "", "2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("operator is blank");

        assertThatThrownBy(() -> c.eval("1", "   ", "2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("operator is blank");

        assertThatThrownBy(() -> c.eval("1", null, "2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("operator is blank");
    }

    @Test
    @DisplayName("Test null operand input throws IllegalArgumentException")
    void testNullOperandThrows() {
        assertThatThrownBy(() -> c.eval(null, "+", "2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("input is blank");

        assertThatThrownBy(() -> c.eval("2", "+", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("input is blank");
    }

    @Test
    @DisplayName("Test non-numeric input throws IllegalArgumentException with cause")
    void testNonNumericInputThrows() {
        assertThatThrownBy(() -> c.eval("abc", "+", "2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("non-numeric input:");

        assertThatThrownBy(() -> c.eval("2", "+", "xyz"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("non-numeric input:");
    }

    @ParameterizedTest
    @ValueSource(strings = {"&", "?", "foo", "++", "**"})
    @DisplayName("Test unknown operator throws IllegalArgumentException")
    void testUnknownOperatorThrows(String unknownOp) {
        assertThatThrownBy(() -> c.eval("2", unknownOp, "3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("unknown operator: " + unknownOp);
    }
}
