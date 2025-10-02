package ru.nsu.ermakov;

import java.util.Map;

/**
 * Base abstract node of an expression AST.
 * All concrete nodes (Number, Variable, Add, Sub, Mul, Div) must implement
 * printable form, evaluation, and symbolic differentiation.
 */
public abstract class Expression {

    /**
     * Builds a new expression that is the symbolic derivative of this expression
     * with respect to the given variable name.
     *
     * <p>Implementations must not mutate this object; return a new tree.</p>
     *
     * @param var variable name (e.g., "x")
     * @return new expression representing the derivative
     */
    public abstract Expression derivative(String var);

    /**
     * Evaluates this expression under the given environment.
     *
     * @param env mapping from variable names to integer values
     * @return integer result of evaluation
     * @throws IllegalArgumentException if a required variable is missing
     * @throws ArithmeticException if an illegal arithmetic operation occurs (e.g., division by zero)
     */
    public abstract int eval(Map<String, Integer> env);

    /**
     * Returns the fully parenthesized textual representation.
     *
     * @return string form with no extra spaces
     */
    public abstract String print();

    @Override
    public final String toString() {
        return print();
    }
}
