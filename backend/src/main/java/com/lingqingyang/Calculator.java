package com.lingqingyang;

import org.apache.commons.lang3.StringUtils;

public class Calculator {

    public double eval(String a, String op, String b) {
        if (StringUtils.isBlank(a) || StringUtils.isBlank(b)) {
            throw new IllegalArgumentException("input is blank");
        }
        if (StringUtils.isBlank(op)) {
            throw new IllegalArgumentException("operator is blank");
        }

        double x;
        double y;
        try {
            x = Double.parseDouble(a.trim());
            y = Double.parseDouble(b.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("non-numeric input: " + e.getMessage(), e);
        }

        return switch (op.trim()) {
            case "+" -> x + y;
            case "-" -> x - y;
            case "*" -> x * y;
            case "/" -> {
                if (y == 0.0) {
                    throw new IllegalArgumentException("division by zero");
                }
                yield x / y;
            }
            case "^" -> Math.pow(x, y);
            case "%" -> {
                if (y == 0.0) {
                    throw new IllegalArgumentException("division by zero");
                }
                yield x % y;
            }
            default -> throw new IllegalArgumentException("unknown operator: " + op);
        };
    }
}
