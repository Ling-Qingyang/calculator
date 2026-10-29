package com.lingqingyang;

import java.io.IOException;

/**
 * Main application entrypoint supporting both CLI evaluation and Microservice HTTP Server mode.
 */
public class App {

    public static void main(String[] args) {
        // Mode 1: CLI calculation mode if exactly 3 arguments are provided (backward compatible with Labs 3-6)
        if (args.length == 3) {
            Calculator c = new Calculator();
            double r = c.eval(args[0], args[1], args[2]);
            System.out.println(r);
            return;
        }

        // Mode 2: Insufficient CLI arguments (1 or 2 args) - display usage help
        if (args.length == 1 && !"server".equalsIgnoreCase(args[0])) {
            System.err.println("Usage: java -cp target/calculator-1.0.jar com.lingqingyang.App <a> <op> <b>");
            return;
        }
        if (args.length == 2) {
            System.err.println("Usage: java -cp target/calculator-1.0.jar com.lingqingyang.App <a> <op> <b>");
            return;
        }

        // Mode 3: Server mode (0 args or explicit "server" argument)
        int port = 8000;
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                port = Integer.parseInt(envPort.trim());
            } catch (NumberFormatException e) {
                System.err.println("[WARN] Invalid PORT environment variable: " + envPort + ", falling back to default 8000");
            }
        }

        try {
            CalculatorServer server = new CalculatorServer(port);
            server.start();
            // Register shutdown hook for graceful termination in Docker container
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("[INFO] Shutdown signal received. Stopping Calculator Server...");
                server.stop(1);
            }));
            // Block main thread to keep microservice container running
            Thread.currentThread().join();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to start HTTP server on port " + port + ": " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("[INFO] Calculator server interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
