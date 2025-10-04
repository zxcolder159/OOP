package ru.nsu.ermakov;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Набор модульных тестов для AST выражений: печать, вычисление и дифференцирование.
 */
public final class ExpressionTest {

    /**
     * Константа: print/eval/toString и производная равная нулю.
     */
    @Test
    void numberPrintEvalAndDerivativeZero() {
        Number c = new Number(42);
        Map<String, Integer> env = new HashMap<>();
        assertEquals("42", c.print());
        assertEquals("42", c.toString());
        assertEquals(42, c.eval(env));
        Expression d = c.derivative("x");
        assertNotNull(d);
        assertTrue(d instanceof Number);
        assertEquals(0, d.eval(env));
    }

    /**
     * Переменная: успешное вычисление и ошибка при отсутствии значения в окружении.
     */
    @Test
    void variableEvalAndMissingBinding() {
        Variable x = new Variable("x");
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 7);
        assertEquals("x", x.print());
        assertEquals(7, x.eval(env));
        assertThrows(IllegalArgumentException.class, () -> x.eval(new HashMap<>()));
    }

    /**
     * Сложение: вычисление и нулевая производная для суммы констант.
     */
    @Test
    void addEvalAndDerivativeOfConstantsIsZero() {
        Expression sum = new Add(new Number(2), new Number(3));
        Map<String, Integer> env = new HashMap<>();
        assertEquals(5, sum.eval(env));
        Expression d = sum.derivative("x");
        assertTrue(d instanceof Number);
        assertEquals(0, d.eval(env));
        assertEquals("(" + "2" + "+" + "3" + ")", sum.print());
    }

    /**
     * Вычитание: вычисление и численная проверка производной.
     */
    @Test
    void subEvalAndDerivativeNumeric() {
        Variable x = new Variable("x");
        Expression expr = new Sub(new Mul(x, new Number(3)), new Number(5));
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 4);
        assertEquals(7, expr.eval(env)); // 3*4 - 5 = 7
        Expression d = expr.derivative("x"); // d/dx(3x - 5) = 3
        assertEquals(3, d.eval(env));
    }

    /**
     * Умножение: правило произведения проверяется численно на f(x)=x*x.
     */
    @Test
    void mulProductRuleNumeric() {
        Variable x = new Variable("x");
        Expression f = new Mul(x, x); // f(x) = x^2
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 6);
        assertEquals(36, f.eval(env));
        Expression df = f.derivative("x"); // f'(x) = 2x
        assertEquals(12, df.eval(env));
    }

    /**
     * Деление: правило частного проверяется численно на f(x)=x^2/2.
     */
    @Test
    void divQuotientRuleNumeric() {
        Variable x = new Variable("x");
        Expression f = new Div(new Mul(x, x), new Number(2));
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 5);
        assertEquals(12, f.eval(env)); // 25/2 округляется вниз, если деление целочисленное
        Expression df = f.derivative("x"); // f'(x) = x (для вещественного), численно проверим
        assertEquals(5, df.eval(env));
    }

    /**
     * Композиция и делегирование toString к print.
     */
    @Test
    void toStringDelegatesToPrint() {
        Expression expr = new Add(
                new Sub(new Number(10), new Variable("y")),
                new Mul(new Variable("x"), new Number(2))
        );
        String printed = expr.print();
        assertEquals(printed, expr.toString());
        Map<String, Integer> env = new HashMap<>();
        env.put("x", 3);
        env.put("y", 4);
        assertEquals(12, expr.eval(env)); // (10 - 4) + (3 * 2) = 6 + 6 = 12
    }
}
