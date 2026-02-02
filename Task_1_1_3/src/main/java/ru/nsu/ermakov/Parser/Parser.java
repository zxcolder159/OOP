package ru.nsu.ermakov.Parser;

import ru.nsu.ermakov.Arifmetic.*;
import ru.nsu.ermakov.Arifmetic.Number;

/**
 * Простой рекурсивный спускающийся парсер для выражений из целых чисел,
 * переменных и операций {@code + - * /} с поддержкой скобок.
 */
public final class Parser {

    private String source;
    private int pos;

    // Конструктор без статического метода
    public Parser(final String s) {
        this.source = s;
        this.pos = 0;
    }

    /**
     * Разобрать строку в AST.
     *
     * @param input входная строка
     * @return корневой узел AST
     */
    public Expression parse(final String input) {
        this.source = input == null ? "" : input;
        this.pos = 0;
        Expression e = parseExpr();
        skipWs();
        if (!eof()) {
            throw new IllegalArgumentException(
                    "Unexpected trailing input at position " + pos
            );
        }
        return e;
    }

    private Expression parseExpr() {
        Expression term = parseTerm();
        while (true) {
            skipWs();
            if (match('+')) {
                term = new Add(term, parseTerm());
                continue;
            }
            if (match('-')) {
                term = new Sub(term, parseTerm());
                continue;
            }
            break;
        }
        return term;
    }

    private Expression parseTerm() {
        Expression factor = parseFactor();
        while (true) {
            skipWs();
            if (match('*')) {
                factor = new Mul(factor, parseFactor());
                continue;
            }
            if (match('/')) {
                factor = new Div(factor, parseFactor());
                continue;
            }
            break;
        }
        return factor;
    }

    private Expression parseFactor() {
        skipWs();
        if (match('(')) {
            Expression inside = parseExpr();
            skipWs();
            expect(')');
            return inside;
        }
        if (peekIsDigit() || peekIsSignFollowedByDigit()) {
            int value = parseInt();
            return new Number(value);
        }
        if (peekIsLetter()) {
            String name = parseName();
            return new Variable(name);
        }
        throw new IllegalArgumentException(
                "Unexpected token at position " + pos
        );
    }

    private boolean peekIsDigit() {
        return !eof() && Character.isDigit(source.charAt(pos));
    }

    private boolean peekIsLetter() {
        return !eof() && Character.isLetter(source.charAt(pos));
    }

    private boolean peekIsSignFollowedByDigit() {
        if (eof()) {
            return false;
        }
        char c = source.charAt(pos);
        if (c != '+' && c != '-') {
            return false;
        }
        return (pos + 1) < source.length()
                && Character.isDigit(source.charAt(pos + 1));
    }

    private int parseInt() {
        skipWs();
        int sign = 1;
        if (!eof() && (source.charAt(pos) == '+' || source.charAt(pos) == '-')) {
            if (source.charAt(pos) == '-') {
                sign = -1;
            }
            pos++;
        }
        int start = pos;
        while (!eof() && Character.isDigit(source.charAt(pos))) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException(
                    "Expected integer at position " + pos
            );
        }
        int val = Integer.parseInt(source.substring(start, pos));
        return sign * val;
    }

    private String parseName() {
        skipWs();
        int start = pos;
        while (!eof()
                && (Character.isLetterOrDigit(source.charAt(pos))
                || source.charAt(pos) == '_')) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException(
                    "Expected name at position " + pos
            );
        }
        return source.substring(start, pos);
    }

    private void skipWs() {
        while (!eof() && Character.isWhitespace(source.charAt(pos))) {
            pos++;
        }
    }

    private boolean match(final char ch) {
        skipWs();
        if (!eof() && source.charAt(pos) == ch) {
            pos++;
            return true;
        }
        return false;
    }

    private void expect(final char ch) {
        skipWs();
        if (eof() || source.charAt(pos) != ch) {
            throw new IllegalArgumentException(
                    "Expected '" + ch + "' at position " + pos
            );
        }
        pos++;
    }

    private boolean eof() {
        return pos >= source.length();
    }
}
