package ru.nsu.ermakov.Arifmetic;

import java.util.Map;

/**
 * Узел-переменная в выражении.
 */
public final class Variable extends Expression {

    /** Имя переменной. */
    private final String name;

    /**
     * Создать переменную с указанным именем.
     *
     * @param name имя переменной (например, {@code "x"})
     */
    public Variable(final String name) {
        this.name = name;
    }

    /**
     * Имя переменной.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }

    @Override
    public int eval(final Map<String, Integer> env) {
        if (env == null || !env.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Variable '" + name + "' is not defined"
            );
        }
        return env.get(name);
    }

    @Override
    public Expression derivative(final String var) {
        return name.equals(var) ? new Number(1) : new Number(0);
    }

    /**
     * Строковое представление переменной.
     *
     * @return имя переменной
     */
    @Override
    public String print() {
        return name;
    }
}
