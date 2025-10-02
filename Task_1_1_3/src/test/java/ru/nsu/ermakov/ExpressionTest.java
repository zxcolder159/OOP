package ru.nsu.ermakov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class ExpressionTest {

    private Expression addExpression;
    private Expression mulExpression;
    private Expression numExpression;
    private Expression varExpression;

    @BeforeEach
    void setUp() {
        addExpression = new Add(new Number(3), new Mul(new Number(2), new Variable("x")));
        mulExpression = new Mul(new Number(2), new Variable("x"));
        numExpression = new Number(5);
        varExpression = new Variable("x");
    }

    @Test
    void testAddExpression() {
        assertNotNull(addExpression);
        assertEquals("(3+(2*x))", addExpression.toString());
    }

    @Test
    void testMulExpression() {
        assertNotNull(mulExpression);
        assertEquals("(2*x)", mulExpression.toString());
    }

    @Test
    void testNumberExpression() {
        assertNotNull(numExpression);
        assertEquals("5", numExpression.toString());
    }

    @Test
    void testVariableExpression() {
        assertNotNull(varExpression);
        assertEquals("x", varExpression.toString());
    }

    @Test
    void testEval() {
        Map<String, Integer> env = Map.of("x", 10);
        int result = addExpression.eval(env);
        assertEquals(23, result); // 3 + (2 * 10) = 23
    }

    @Test
    void testDerivative() {
        Expression derivative = addExpression.derivative("x");
        assertNotNull(derivative);
        assertEquals("(0+((0*x)+(2*1)))", derivative.toString()); // Derivative of (3+(2*x)) w.r.t x
    }

    @Test
    void testAssignments() {
        String assignments = "x = 10; y = 20";
        Map<String, Integer> env = Assignments.parseEnv(assignments);
        assertEquals(10, env.get("x"));
        assertEquals(20, env.get("y"));
    }

    @Test
    void testParser() {
        String expression = "3 + (2 * x)";
        Expression parsedExpr = Parser.parse(expression);
        assertNotNull(parsedExpr);
        assertEquals("(3+(2*x))", parsedExpr.toString());
    }
}
