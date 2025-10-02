package ru.nsu.ermakov;

import java.util.Map;

/**
 * Mul node representing the multiplication operation in an expression.
 */
public final class Mul extends Expression {

    /** Left operand of the multiplication. */
    private final Expression left;

    /** Right operand of the multiplication. */
    private final Expression right;

    /**
     * Constructs a Mul node with the given operands.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Mul(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the symbolic derivative of the multiplication.
     *
     * d(A * B) = A' * B + A * B'
     *
     * @param var the variable name to differentiate with respect to
     * @return a new {@link Add} representing the derivative
     */
    @Override
    public Expression derivative(final String var) {
        Expression leftDeriv = left.derivative(var);  // A'
        Expression rightDeriv = right.derivative(var);  // B'
        Expression leftTerm = new Mul(leftDeriv, right);  // A' * B
        Expression rightTerm = new Mul(left, rightDeriv);  // A * B'
        return new Add(leftTerm, rightTerm);  // A' * B + A * B'
    }

    /**
     * Evaluates the value of this multiplication.
     *
     * @param env the environment mapping variable names to values
     * @return the result of multiplying the left and right operands
     */
    @Override
    public int eval(final Map<String, Integer> env) {
        return left.eval(env) * right.eval(env);
    }

    /**
     * Returns the string representation of this multiplication.
     *
     * @return the parenthesized string representation of the multiplication
     */
    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }
}
