package ru.nsu.ermakov.checker;

public record TestStats(int passed, int failed, int skipped) {

    public static TestStats empty() {
        return new TestStats(0, 0, 0);
    }

    public int total() {
        return passed + failed + skipped;
    }
}
