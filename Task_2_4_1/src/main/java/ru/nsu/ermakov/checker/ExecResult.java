package ru.nsu.ermakov.checker;

public record ExecResult(boolean success, String output) {
    public static ExecResult failure(String message) {
        return new ExecResult(false, message == null ? "" : message);
    }
}
