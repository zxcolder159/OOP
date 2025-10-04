package ru.nsu.ermakov;

import java.util.HashMap;
import java.util.Map;

/**
 * Утилита для разбора окружения переменных из строки вида
 * {@code "x=1; y=-2; z = 0"}.
 */
public final class Assignments {

    private Assignments() {
        // utility
    }

    /**
     * Разобрать строку присваиваний в отображение.
     *
     * @param input строка формата {@code name=value; name=value; ...}
     * @return отображение переменных
     */
    public static Map<String, Integer> parseEnv(final String input) {
        Map<String, Integer> env = new HashMap<>();
        if (input == null) {
            return env;
        }
        String[] parts = input.split(";");
        for (String raw : parts) {
            String part = raw.trim();
            if (part.isEmpty()) {
                continue;
            }
            int eq = part.indexOf('=');
            if (eq <= 0 || eq >= part.length() - 1) {
                throw new IllegalArgumentException(
                        "Bad assignment: '" + part + "'"
                );
            }
            String name = part.substring(0, eq).trim();
            String valueStr = part.substring(eq + 1).trim();
            int value;
            try {
                value = Integer.parseInt(valueStr);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(
                        "Bad integer: '" + valueStr + "'"
                );
            }
            env.put(name, value);
        }
        return env;
    }
}
