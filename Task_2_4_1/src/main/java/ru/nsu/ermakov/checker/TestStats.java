package ru.nsu.ermakov.checker;

/**
 * Статистика выполнения тестов.
 * @param passed число пройденных тестов
 * @param failed число проваленных тестов
 * @param skipped число пропущенных тестов
 */
public record TestStats(int passed, int failed, int skipped) {

    /**
     * Пустая статистика (все счётчики равны нулю).
     */
    public static TestStats empty() {
        return new TestStats(0, 0, 0);
    }

    /**
     * Общее число тестов.
     * @return сумма passed, failed и skipped
     */
    public int total() {
        return passed + failed + skipped;
    }
}
