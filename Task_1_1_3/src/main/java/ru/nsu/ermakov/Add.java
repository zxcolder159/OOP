package ru.nsu.ermakov;

import java.util.Map;

/**
 * Узел суммирования двух подвыражений.
 *
 * <p>Печатается в виде {@code (left+right)}.
 */
public final class Add extends Expression {

    private final Expression left;
    private final Expression right;

    /**
     * Создать сумму.
     * @param left левый операнд
     * @param right правый операнд
     */
    public Add(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        return left.eval(env) + right.eval(env);
    }

    @Override
    public Expression derivative(final String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }

    @Override
    public String print() {
        return "(" + left.print() + "+" + right.print() + ")";
    }
}
