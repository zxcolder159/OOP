package ru.nsu.ermakov;

import java.util.Map;

/**
 * Простой рекурсивный спускающийся парсер для выражений из целых чисел,
 * переменных и операций {@code + - * /} с поддержкой скобок.
 */
public final class Parser {

    private final String s;
    private int pos;

    private Parser(final String s) {
        this.s = s;
        this.pos = 0;
    }

    /**
     * Разобрать строку в AST.
     *
     * @param input входная строка
     * @return корневой узел AST
     */
    public static Expression parse(final String input) {
        Parser p = new Parser(input == null ? "" : input);
        Expression e = p.parseExpr();
        p.skipWs();
        if (!p.eof()) {
            throw new IllegalArgumentException(
                    "Unexpected trailing input at position " + p.pos
            );
        }
        return e;
    }

    private Expression parseExpr() {
        Expression term = parseTerm();
        while (true) {
            skipWs();
            if (match('+')) {
                Expression rhs = parseTerm();
                term = new Add(term, rhs);
                continue;
            }
            if (match('-')) {
                Expression rhs = parseTerm();
                term = new Sub(term, rhs);
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
                Expression rhs = parseFactor();
                factor = new Mul(factor, rhs);
                continue;
            }
            if (match('/')) {
                Expression rhs = parseFactor();
                factor = new Div(factor, rhs);
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
        return !eof() && Character.isDigit(s.charAt(pos));
    }

    private boolean peekIsLetter() {
        return !eof() && Character.isLetter(s.charAt(pos));
    }

    private boolean peekIsSignFollowedByDigit() {
        if (eof()) {
            return false;
        }
        char c = s.charAt(pos);
        if (c != '+' && c != '-') {
            return false;
        }
        return (pos + 1) < s.length() && Character.isDigit(s.charAt(pos + 1));
    }

    private int parseInt() {
        skipWs();
        int sign = 1;
        if (!eof() && (s.charAt(pos) == '+' || s.charAt(pos) == '-')) {
            if (s.charAt(pos) == '-') {
                sign = -1;
            }
            pos++;
        }
        int start = pos;
        while (!eof() && Character.isDigit(s.charAt(pos))) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException(
                    "Expected integer at position " + pos
            );
        }
        int val = Integer.parseInt(s.substring(start, pos));
        return sign * val;
    }

    private String parseName() {
        skipWs();
        int start = pos;
        while (!eof() && (Character.isLetterOrDigit(s.charAt(pos))
                || s.charAt(pos) == '_')) {
            pos++;
        }
        if (start == pos) {
            throw new IllegalArgumentException(
                    "Expected name at position " + pos
            );
        }
        return s.substring(start, pos);
    }

    private void skipWs() {
        while (!eof() && Character.isWhitespace(s.charAt(pos))) {
            pos++;
        }
    }

    private boolean match(final char ch) {
        skipWs();
        if (!eof() && s.charAt(pos) == ch) {
            pos++;
            return true;
        }
        return false;
    }

    private void expect(final char ch) {
        skipWs();
        if (eof() || s.charAt(pos) != ch) {
            throw new IllegalArgumentException(
                    "Expected '" + ch + "' at position " + pos
            );
        }
        pos++;
    }

    private boolean eof() {
        return pos >= s.length();
    }
}
