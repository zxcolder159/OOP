package ru.nsu.ermakov;

import java.util.Map;

/**
 * Parser to convert a string expression into an expression tree.
 */
public final class Parser {

    /** The input string to parse. */
    private final String input;

    /** Current position in the string. */
    private int pos;

    /**
     * Creates a new parser for the given input expression.
     *
     * @param input the expression to parse
     */
    public Parser(String input) {
        this.input = input.replaceAll("\\s+", "");
        this.pos = 0;
    }

    /**
     * Parses the entire expression.
     *
     * @return the parsed expression
     */
    public static Expression parse(String input) {
        Parser parser = new Parser(input);
        return parser.parseExpr();
    }

    /**
     * Parses the expression starting from the current position.
     *
     * @return the parsed expression
     */
    private Expression parseExpr() {
        Expression left = parseTerm();

        while (pos < input.length()) {
            char op = peek();
            if (op == '+' || op == '-') {  // если плюс или минус
                next();  // съесть символ
                Expression right = parseTerm();
                if (op == '+') {
                    left = new Add(left, right);
                } else {
                    left = new Sub(left, right);
                }
            } else {
                break;
            }
        }
        return left;
    }

    /**
     * Parses a term (multiplication/division).
     *
     * @return the parsed term
     */
    private Expression parseTerm() {
        Expression left = parseFactor();  // начинаем с "фактора" (число, переменная, скобки)

        while (pos < input.length()) {
            char op = peek();
            if (op == '*' || op == '/') {  // если умножение или деление
                next();  // съесть символ
                Expression right = parseFactor();
                if (op == '*') {
                    left = new Mul(left, right);
                } else {
                    left = new Div(left, right);
                }
            } else {
                break;
            }
        }
        return left;
    }

    /**
     * Parses a factor (number, variable, or parentheses).
     *
     * @return the parsed factor
     */
    private Expression parseFactor() {
        if (peek() == '(') {
            next();  // съесть '('
            Expression expr = parseExpr();  // рекурсивный разбор выражения в скобках
            expect(')');  // ожидаем ')'
            return expr;
        } else if (Character.isDigit(peek())) {
            return parseNumber();  // парсим число
        } else if (Character.isLetter(peek())) {
            return parseVariable();  // парсим переменную
        } else {
            throw new IllegalArgumentException("Unexpected character: " + peek());
        }
    }

    /**
     * Parses a number.
     *
     * @return the parsed number
     */
    private Expression parseNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(peek())) {
            sb.append(next());  // собираем цифры
        }
        return new Number(Integer.parseInt(sb.toString()));  // создаём Number
    }

    /**
     * Parses a variable.
     *
     * @return the parsed variable
     */
    private Expression parseVariable() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (Character.isLetter(peek()) || Character.isDigit(peek()))) {
            sb.append(next());  // собираем символы для переменной
        }
        return new Variable(sb.toString());  // создаём Variable
    }

    // ===================== Утилитарные методы =====================

    /**
     * Returns the current character without advancing the position.
     *
     * @return the current character
     */
    private char peek() {
        if (pos >= input.length()) {
            return '\0';  // конец строки
        }
        return input.charAt(pos);
    }

    /**
     * Advances the position and returns the current character.
     *
     * @return the current character
     */
    private char next() {
        return input.charAt(pos++);
    }

    /**
     * Ensures that the next character is the expected one.
     *
     * @param expected the expected character
     */
    private void expect(char expected) {
        if (peek() != expected) {
            throw new IllegalArgumentException("Expected '" + expected + "' but found '" + peek() + "'");
        }
        next();  // съесть символ
    }
}
