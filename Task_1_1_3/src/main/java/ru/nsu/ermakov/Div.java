package ru.nsu.ermakov;

import java.util.Map;

/**
 * Div node representing the division operation in an expression.
 */
public final class Div extends Expression {

    /** Left operand of the division. */
    private final Expression left;

    /** Right operand of the division. */
    private final Expression right;

    /**
     * Constructs a Div node with the given operands.
     *
     * @param left the left operand
     * @param right the right operand
     */
    public Div(final Expression left, final Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the symbolic derivative of the division.
     *
     * d(A / B) = (A' * B - A * B') / B^2
     *
     * @param var the variable name to differentiate with respect to
     * @return a new {@link Div} representing the derivative
     */
    @Override
    public Expression derivative(final String var) {
        Expression leftDeriv = left.derivative(var);  // d(A)/dx
        Expression rightDeriv = right.derivative(var);  // d(B)/dx
        Expression numerator = new Sub(new Mul(leftDeriv, right), new Mul(left, rightDeriv));  // A' * B - A * B'
        Expression denominator = new Mul(right, right);  // B * B
        return new Div(numerator, denominator);  // (A' * B - A * B') / B^2
    }

    /**
     * Evaluates the value of this division.
     *
     * @param env the environment mapping variable names to values
     * @return the result of dividing the left operand by the right operand
     * @throws ArithmeticException if division by zero occurs
     */
    @Override
    public int eval(final Map<String, Integer> env) {
        int leftValue = left.eval(env);
        int rightValue = right.eval(env);
        if (rightValue == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return leftValue / rightValue;
    }

    /**
     * Returns the string representation of this division.
     *
     * @return the parenthesized string representation of the division
     */
    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }
}
