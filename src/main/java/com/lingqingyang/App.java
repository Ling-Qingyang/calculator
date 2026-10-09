package com.lingqingyang;

public class App {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: java -cp target/calculator-1.0.jar com.lingqingyang.App <a> <op> <b>");
            return;
        }
        Calculator c = new Calculator();
        double r = c.eval(args[0], args[1], args[2]);
        System.out.println(r);
    }
}
