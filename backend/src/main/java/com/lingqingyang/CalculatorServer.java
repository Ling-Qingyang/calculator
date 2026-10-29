package com.lingqingyang;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lightweight HTTP REST API Server for the Calculator Microservice.
 * Built using JDK standard HttpServer without external runtime dependencies.
 */
public class CalculatorServer {

    private final int port;
    private final Calculator calculator;
    private HttpServer server;

    public CalculatorServer(int port) {
        this.port = port;
        this.calculator = new Calculator();
    }

    public synchronized void start() throws IOException {
        if (server != null) {
            return;
        }
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        // Health check endpoint for Docker Compose healthcheck & readiness
        server.createContext("/health", new HealthHandler());
        server.createContext("/api/health", new HealthHandler());

        // Calculation API endpoint
        CalculateHandler calcHandler = new CalculateHandler(calculator);
        server.createContext("/api/calculate", calcHandler);
        server.createContext("/calculate", calcHandler);

        server.start();
        System.out.println("[INFO] Calculator Backend API Server successfully started on port " + port);
    }

    public synchronized void stop(int delaySeconds) {
        if (server != null) {
            server.stop(delaySeconds);
            server = null;
            System.out.println("[INFO] Calculator Backend API Server stopped.");
        }
    }

    public int getPort() {
        return port;
    }

    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        setCorsHeaders(exchange);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                setCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }
            String json = "{\"status\":\"UP\",\"service\":\"calculator-backend\",\"port\":" + exchange.getLocalAddress().getPort() + "}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    static class CalculateHandler implements HttpHandler {
        private final Calculator calculator;
        private static final Pattern PATTERN_A = Pattern.compile("\"a\"\\s*:\\s*\"?([^\",}]+)\"?");
        private static final Pattern PATTERN_B = Pattern.compile("\"b\"\\s*:\\s*\"?([^\",}]+)\"?");
        private static final Pattern PATTERN_OP = Pattern.compile("\"op\"\\s*:\\s*\"([^\"]+)\"");

        public CalculateHandler(Calculator calculator) {
            this.calculator = calculator;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                setCorsHeaders(exchange);
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String a = null;
            String op = null;
            String b = null;

            String method = exchange.getRequestMethod().toUpperCase();
            if ("GET".equals(method)) {
                String query = exchange.getRequestURI().getRawQuery();
                if (query != null) {
                    Map<String, String> params = parseQuery(query);
                    a = params.get("a");
                    op = params.get("op");
                    b = params.get("b");
                }
            } else if ("POST".equals(method)) {
                StringBuilder bodyBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        bodyBuilder.append(line);
                    }
                }
                String body = bodyBuilder.toString().trim();
                if (body.startsWith("{") && body.endsWith("}")) {
                    Matcher ma = PATTERN_A.matcher(body);
                    Matcher mb = PATTERN_B.matcher(body);
                    Matcher mop = PATTERN_OP.matcher(body);
                    if (ma.find()) a = ma.group(1).trim();
                    if (mb.find()) b = mb.group(1).trim();
                    if (mop.find()) op = mop.group(1).trim();
                } else {
                    Map<String, String> params = parseQuery(body);
                    a = params.get("a");
                    op = params.get("op");
                    b = params.get("b");
                }
            }

            if (a == null || op == null || b == null) {
                String err = "{\"status\":\"error\",\"message\":\"Missing required parameters: 'a', 'op', and 'b' are mandatory.\"}";
                sendJsonResponse(exchange, 400, err);
                return;
            }

            try {
                double result = calculator.eval(a, op, b);
                String safeOp = op.replace("\"", "\\\"");
                String json = String.format("{\"status\":\"success\",\"a\":\"%s\",\"op\":\"%s\",\"b\":\"%s\",\"result\":%s}",
                        escapeJson(a), escapeJson(safeOp), escapeJson(b), result);
                sendJsonResponse(exchange, 200, json);
            } catch (IllegalArgumentException e) {
                String err = String.format("{\"status\":\"error\",\"message\":\"%s\"}", escapeJson(e.getMessage()));
                sendJsonResponse(exchange, 400, err);
            } catch (Exception e) {
                String err = String.format("{\"status\":\"error\",\"message\":\"Internal server error: %s\"}", escapeJson(e.getMessage()));
                sendJsonResponse(exchange, 500, err);
            }
        }

        private Map<String, String> parseQuery(String query) {
            Map<String, String> map = new HashMap<>();
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0 && idx < pair.length() - 1) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String val = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    map.put(key, val);
                }
            }
            return map;
        }

        private String escapeJson(String s) {
            if (s == null) return "";
            return s.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}
