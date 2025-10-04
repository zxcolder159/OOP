package ru.nsu.ermakov;

import java.util.Map;

/**
 * Листовой узел AST, представляющий целочисленную константу.
 */
public final class Number extends Expression {

    /** Значение константы. */
    private final int value;

    /**
     * Создать константу.
     *
     * @param value целочисленное значение
     */
    public Number(final int value) {
        this.value = value;
    }

    /**
     * Получить значение константы.
     *
     * @return значение
     */
    public int getValue() {
        return value;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        return value;
    }

    @Override
    public Expression derivative(final String var) {
        return new Number(0);
    }

    @Override
    public String print() {
        return Integer.toString(value);
    }
}
