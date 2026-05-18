package ru.nsu.ermakov.checker;

/**
 * Статистика активности по неделям.
 * @param activeWeeks число недель с активностью
 * @param totalWeeks общее число недель в периоде
 * @param ratio отношение активных недель к общему числу
 */
public record ActivityStats(int activeWeeks, int totalWeeks, double ratio) {

    /**
     * Пустая статистика активности.
     */
    public static ActivityStats empty() {
        return new ActivityStats(0, 0, 0.0);
    }
}
