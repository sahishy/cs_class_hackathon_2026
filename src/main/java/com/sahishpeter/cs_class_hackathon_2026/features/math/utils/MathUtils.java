package com.sahishpeter.cs_class_hackathon_2026.features.math.utils;

import net.objecthunter.exp4j.Expression;

public class MathUtils {
    
    public static double evaluateFunction(Expression expression, double x) {

        try {

            expression.setVariable("x", x);
            return expression.evaluate();

        } catch (RuntimeException ex) {
            return Double.NaN;
        }

    }

    public static String normalizeFunction(String func) {

        String normalized = func.toLowerCase().trim();
        normalized = normalized.replaceAll("^y\\s*=\\s*", "");
        normalized = normalized.replaceAll("\\s+", "");

        // support stuff like 2x, x2, x(x+1), (x+1)(x-1), 2(x+1)
        normalized = normalized.replaceAll("(?<=[0-9)])(?=[a-z(])", "*");
        normalized = normalized.replaceAll("(?<=[a-z])(?=[0-9(])", "*");

        return normalized;

    }

}
