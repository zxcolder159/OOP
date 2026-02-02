package ru.nsu.ermakov.Arifmetic;

import java.util.Map;

/**
 * Узел вычитания двух подвыражений.
 *
 * <p>Печатается в виде {@code (left-right)}.
 */
public final class Sub extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Создать разность.
     * @param left уменьшаемое
     * @param right вычитаемое
     */
    public Sub(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        return left.eval(env) - right.eval(env);
    }

    @Override
    public Expression derivative(final String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }
}
