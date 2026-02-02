package ru.nsu.ermakov.Arifmetic;

import java.util.Map;

/**
 * Базовый класс для всех AST-узлов арифметических выражений.
 *
 * <p>Поддерживает вычисление, печать и символическое дифференцирование.
 */
public abstract class Expression {

    /**
     * Вычислить значение выражения в окружении переменных.
     *
     * @param env отображение имя переменной → значение
     * @return целочисленный результат вычисления
     */
    public abstract int eval(Map<String, Integer> env);

    /**
     * Построить символическую производную по переменной {@code var}.
     *
     * @param var имя переменной, по которой берётся производная
     * @return новый AST — производная исходного выражения
     */
    public abstract Expression derivative(String var);

    /**
     * Получить строковое представление выражения.
     *
     * @return строка, описывающая выражение
     */
    protected abstract String print();

    @Override
    public final String toString() {
        return print();
    }
}
