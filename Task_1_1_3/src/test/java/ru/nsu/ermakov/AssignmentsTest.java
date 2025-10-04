package ru.nsu.ermakov;

import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты разбора окружения переменных из строковых присваиваний.
 */
public final class AssignmentsTest {

    /**
     * Корректный разбор: поддерживаются пробелы и знак минус.
     */
    @Test
    void parsesValidEnv() {
        Map<String, Integer> env = Assignments.parseEnv("x = 10; y=-2;  z = 0");
        assertEquals(3, env.size());
        assertEquals(10, env.get("x").intValue());
        assertEquals(-2, env.get("y").intValue());
        assertEquals(0, env.get("z").intValue());
    }

    /**
     * Неверные числа вызывают IllegalArgumentException.
     */
    @Test
    void rejectsInvalidNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> Assignments.parseEnv("x=10; y=oops"));
        assertThrows(IllegalArgumentException.class,
                () -> Assignments.parseEnv("a=1; b=1.5"));
        assertThrows(IllegalArgumentException.class,
                () -> Assignments.parseEnv("k=--1"));
    }
}
