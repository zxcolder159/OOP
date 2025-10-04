package ru.nsu.ermakov;

import java.util.Map;

/**
 * Узел деления двух подвыражений.
 *
 * <p>Печатается в виде {@code (left/right)}.
 */
public final class Div extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Создать частное.
     * @param left числитель
     * @param right знаменатель
     */
    public Div(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        int denom = right.eval(env);
        if (denom == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return left.eval(env) / denom;
    }

    @Override
    public Expression derivative(final String var) {
        // (u/v)' = (u'v - uv') / v^2
        Expression leftPrimeTimesRight =
                new Mul(left.derivative(var), right);
        Expression leftTimesRightPrime =
                new Mul(left, right.derivative(var));
        Expression numerator =
                new Sub(leftPrimeTimesRight, leftTimesRightPrime);
        Expression denominator = new Mul(right, right);
        return new Div(numerator, denominator);
    }

    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }
}
