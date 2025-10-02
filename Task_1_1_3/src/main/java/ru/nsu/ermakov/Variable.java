package ru.nsu.ermakov;

import java.util.Map;

/**
 * Variable node representing a variable in the expression.
 */
public final class Variable extends Expression {

    /** Name of the variable. */
    private final String name;

    /**
     * Constructs a Variable node with the given name.
     *
     * @param name the name of the variable (e.g., "x")
     */
    public Variable(final String name) {
        this.name = name;
    }

    /**
     * Returns the variable's name.
     *
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the symbolic derivative of the variable.
     *
     * If the variable is the same as the given one, returns 1.
     * Otherwise, returns 0.
     *
     * @param var the variable name to differentiate with respect to
     * @return a new {@link Number} representing the derivative
     */
    @Override
    public Expression derivative(final String var) {
        if (this.name.equals(var)) {
            return new Number(1);
        }
        return new Number(0);
    }

    /**
     * Evaluates the value of this variable from the environment.
     *
     * @param env the environment mapping variable names to values
     * @return the value of the variable from the environment
     * @throws IllegalArgumentException if the variable is not found in the environment
     */
    @Override
    public int eval(final Map<String, Integer> env) {
        Integer value = env.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable '" + name + "' is not defined in the environment.");
        }
        return value;
    }

    /**
     * Returns the string representation of the variable.
     *
     * @return the name of the variable
     */
    @Override
    public String print() {
        return name;
    }
}
