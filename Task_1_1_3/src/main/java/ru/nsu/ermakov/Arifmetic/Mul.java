package ru.nsu.ermakov.Arifmetic;

import java.util.Map;

/**
 * Узел умножения двух подвыражений.
 *
 * <p>Печатается в виде {@code (left*right)}.
 */
public final class Mul extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Создать произведение.
     * @param left левый множитель
     * @param right правый множитель
     */
    public Mul(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        return left.eval(env) * right.eval(env);
    }

    @Override
    public Expression derivative(final String var) {
        // (uv)' = u'v + uv'
        Expression leftPrimeTimesRight =
                new Mul(left.derivative(var), right);
        Expression leftTimesRightPrime =
                new Mul(left, right.derivative(var));
        return new Add(leftPrimeTimesRight, leftTimesRightPrime);
    }

    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }
}
