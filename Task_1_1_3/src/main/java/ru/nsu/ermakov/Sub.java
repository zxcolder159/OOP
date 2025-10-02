package ru.nsu.ermakov;

import java.util.Map;

/**
 * Sub node representing the addition operation in an expression.
 */
public final class Sub extends Expression {

    /** Left operand of the addition. */
    private final Expression left;

    /** Right operand of the addition. */
    private final Expression right;

    /**
     * Constructs an Sub node with the given operands.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Sub(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the symbolic derivative of the addition.
     *
     * d(A - B) = d(A) - d(B)
     *
     * @param var the variable name to differentiate with respect to
     * @return a new {@link Add} representing the derivative
     */
    @Override
    public Expression derivative(final String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    /**
     * Evaluates the value of this addition.
     *
     * @param env the environment mapping variable names to values
     * @return the sum of the values of the left and right operands
     */
    @Override
    public int eval(final Map<String, Integer> env) {
        return left.eval(env) - right.eval(env);
    }

    /**
     * Returns the string representation of this addition.
     *
     * @return the parenthesized string representation of the addition
     */
    @Override
    public String print() {
        return "(" + left.print() + "-" + right.print() + ")";
    }
}
