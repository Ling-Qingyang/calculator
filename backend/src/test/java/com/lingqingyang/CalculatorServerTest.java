package com.lingqingyang;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatorServerTest {

    private static CalculatorServer server;
    private static final int TEST_PORT = 18080;
    private static final String BASE_URL = "http://127.0.0.1:" + TEST_PORT;

    @BeforeAll
    static void setUp() throws Exception {
        server = new CalculatorServer(TEST_PORT);
        server.start();
    }

    @AfterAll
    static void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    @DisplayName("Health endpoint returns 200 and UP status")
    void testHealthEndpoint() throws Exception {
        URL url = new URL(BASE_URL + "/health");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertThat(conn.getResponseCode()).isEqualTo(200);
        String body = readResponse(conn);
        assertThat(body).contains("\"status\":\"UP\"");
        assertThat(body).contains("\"service\":\"calculator-backend\"");
    }

    @Test
    @DisplayName("GET /api/calculate evaluates addition correctly")
    void testGetCalculateAddition() throws Exception {
        URL url = new URL(BASE_URL + "/api/calculate?a=15&op=%2B&b=25");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertThat(conn.getResponseCode()).isEqualTo(200);
        String body = readResponse(conn);
        assertThat(body).contains("\"status\":\"success\"");
        assertThat(body).contains("\"result\":40.0");
    }

    @Test
    @DisplayName("POST /api/calculate evaluates multiplication with JSON body")
    void testPostCalculateJson() throws Exception {
        URL url = new URL(BASE_URL + "/api/calculate");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonPayload = "{\"a\":\"12\",\"op\":\"*\",\"b\":\"6\"}";
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
        }

        assertThat(conn.getResponseCode()).isEqualTo(200);
        String body = readResponse(conn);
        assertThat(body).contains("\"status\":\"success\"");
        assertThat(body).contains("\"result\":72.0");
    }

    @Test
    @DisplayName("GET /api/calculate division by zero returns HTTP 400 error")
    void testDivisionByZero() throws Exception {
        URL url = new URL(BASE_URL + "/api/calculate?a=10&op=%2F&b=0");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertThat(conn.getResponseCode()).isEqualTo(400);
        String body = readError(conn);
        assertThat(body).contains("\"status\":\"error\"");
        assertThat(body).contains("division by zero");
    }

    @Test
    @DisplayName("GET /api/calculate missing parameters returns HTTP 400")
    void testMissingParameters() throws Exception {
        URL url = new URL(BASE_URL + "/api/calculate?a=10");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertThat(conn.getResponseCode()).isEqualTo(400);
        String body = readError(conn);
        assertThat(body).contains("Missing required parameters");
    }

    private static String readResponse(HttpURLConnection conn) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static String readError(HttpURLConnection conn) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
