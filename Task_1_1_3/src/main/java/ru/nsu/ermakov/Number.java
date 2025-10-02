package ru.nsu.ermakov;

import java.util.Map;

/**
 * Leaf AST node that represents an integer constant in a mathematical expression.
 */
public final class Number extends Expression {

    /** Immutable integer value of this constant node. */
    private final int value;

    /**
     * Creates a constant number node.
     *
     * @param value integer value of the constant (may be negative, zero, or positive)
     */
    public Number(final int value) {
        this.value = value;
    }

    /**
     * Returns the textual representation of this number without spaces.
     *
     * @return string form, e.g. {@code "3"} or {@code "-12"}
     */
    @Override
    public String print() {
        return Integer.toString(value);
    }

    /**
     * Evaluates this constant, ignoring the environment.
     *
     * @param env mapping of variable names to values (ignored for constants)
     * @return the stored integer value
     */
    @Override
    public int eval(final Map<String, Integer> env) {
        return value;
    }

    /**
     * Symbolic derivative of a constant is zero.
     *
     * @param var variable name (ignored)
     * @return a new {@link Number} equal to zero
     */
    @Override
    public Expression derivative(final String var) {
        return new Number(0);
    }
}
