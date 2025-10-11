package ru.nsu.ermakov;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты парсера: приоритеты, скобки, переменные и производная.
 */
public final class ParserTest {

    /**
     * Проверка приоритетов и скобок: 2 + 3 * 4 = 14, (2 + 3) * 4 = 20.
     */
    @Test
    void precedenceAndParentheses() {
        // Создаем экземпляр Parser с выражением
        Parser parser1 = new Parser("2+3*4");
        // Парсим выражение
        Expression e1 = parser1.parse("2+3*4");
        assertEquals(14, e1.eval(new HashMap<>()));

        // Создаем второй экземпляр Parser с другим выражением
        Parser parser2 = new Parser("(2+3)*4");
        // Парсим выражение
        Expression e2 = parser2.parse("(2+3)*4");
        assertEquals(20, e2.eval(new HashMap<>()));
    }

    /**
     * Переменные и пробелы: (x + 2) * 3 - y при x=4, y=5 равно 13.
     */
    @Test
    void variablesAndSpaces() {
        // Создаем экземпляр Parser с выражением
        Parser parser = new Parser(" ( x + 2 ) * 3 - y ");
        // Парсим выражение
        Expression e = parser.parse(" ( x + 2 ) * 3 - y ");

        // Создаем карту для значений переменных
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 4);
        env.put("y", 5);

        // Проверяем, что результат вычисления равен 13
        assertEquals(13, e.eval(env));
    }

    /**
     * Дифференцирование: d/dx (x*x + 5) в точке x=3 равно 6.
     */
    @Test
    void derivativeNumericCheck() {
        // Создаем экземпляр Parser с выражением
        Parser parser = new Parser("x*x + 5");

        // Парсим выражение
        Expression e = parser.parse("x*x + 5");

        // Находим производную выражения
        Expression d = e.derivative("x");

        // Создаем карту для значений переменных
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 3);

        // Проверяем, что значение производной при x=3 равно 6
        assertEquals(6, d.eval(env));
    }
}
