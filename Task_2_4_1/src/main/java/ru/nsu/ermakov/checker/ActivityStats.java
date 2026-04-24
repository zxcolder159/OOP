package ru.nsu.ermakov.checker;

public record ActivityStats(int activeWeeks, int totalWeeks, double ratio) {

    public static ActivityStats empty() {
        return new ActivityStats(0, 0, 0.0);
    }
}
