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
        Expression e1 = Parser.parse("2+3*4");
        assertEquals(14, e1.eval(new HashMap<>()));

        Expression e2 = Parser.parse("(2+3)*4");
        assertEquals(20, e2.eval(new HashMap<>()));
    }

    /**
     * Переменные и пробелы: (x + 2) * 3 - y при x=4, y=5 равно 13.
     */
    @Test
    void variablesAndSpaces() {
        Expression e = Parser.parse(" ( x + 2 ) * 3 - y ");
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 4);
        env.put("y", 5);
        assertEquals(13, e.eval(env));
    }

    /**
     * Дифференцирование: d/dx (x*x + 5) в точке x=3 равно 6.
     */
    @Test
    void derivativeNumericCheck() {
        Expression e = Parser.parse("x*x + 5");
        Expression d = e.derivative("x");
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 3);
        assertEquals(6, d.eval(env));
    }
}
