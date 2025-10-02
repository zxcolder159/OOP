package ru.nsu.ermakov;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing environment assignments of variables.
 *
 * <p>This class provides a method to parse variable assignments from a string,
 * creating a mapping of variable names to their integer values.</p>
 */
public final class Assignments {

    /**
     * Parses a string of variable assignments into a map of variable names to values.
     *
     * <p>The input string should be in the format "var1 = value1; var2 = value2; ...".
     * Whitespace around variable names and values will be trimmed.</p>
     *
     * @param s the input string containing assignments (e.g. "x = 10; y = 20")
     * @return a map containing the variable names as keys and their integer values
     * @throws IllegalArgumentException if the format is incorrect
     */
    public static Map<String, Integer> parseEnv(final String s) {
        Map<String, Integer> env = new HashMap<>();

        String[] assignments = s.split(";");

        for (String assignment : assignments) {
            String[] parts = assignment.split("=");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid assignment format: " + assignment);
            }

            String var = parts[0].trim();
            int value;

            try {
                value = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + parts[1].trim());
            }

            env.put(var, value);
        }

        return env;
    }
}
