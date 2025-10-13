package ru.nsu.ermakov;

import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.Parser.Assignments;

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
        // Создаем экземпляр Assignments
        Assignments assignmentsParser = new Assignments();

        // Разбираем строку
        Map<String, Integer> env = assignmentsParser.parseEnv("x = 10; y=-2;  z = 0");

        // Проверяем результаты
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
        // Создаем экземпляр Assignments
        Assignments assignmentsParser = new Assignments();

        // Проверяем выбрасывание исключения для разных ошибок
        assertThrows(IllegalArgumentException.class,
                () -> assignmentsParser.parseEnv("x=10; y=oops"));
        assertThrows(IllegalArgumentException.class,
                () -> assignmentsParser.parseEnv("a=1; b=1.5"));
        assertThrows(IllegalArgumentException.class,
                () -> assignmentsParser.parseEnv("k=--1"));
    }
}
