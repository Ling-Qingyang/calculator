package com.lingqingyang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    @Test
    @DisplayName("Test App constructor")
    void testAppConstructor() {
        App app = new App();
        assertThat(app).isNotNull();
    }

    @Test
    @DisplayName("Test App main prints usage on insufficient arguments")
    void testMainInsufficientArgs() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        try {
            System.setErr(new PrintStream(errContent));
            App.main(new String[]{"2", "+"});
            assertThat(errContent.toString()).contains("Usage: java -cp target/calculator-1.0.jar com.lingqingyang.App");
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    @DisplayName("Test App main executes calculation and prints result")
    void testMainSuccess() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        try {
            System.setOut(new PrintStream(outContent));
            App.main(new String[]{"3", "*", "4"});
            assertThat(outContent.toString().trim()).isEqualTo("12.0");
        } finally {
            System.setOut(originalOut);
        }
    }
}
