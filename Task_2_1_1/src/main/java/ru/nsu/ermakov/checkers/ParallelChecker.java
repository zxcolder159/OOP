package ru.nsu.ermakov.checkers;

import ru.nsu.ermakov.IsPrime;
import java.util.Arrays;

/**
 * Решение с использование StreamAPI.
 */
public class ParallelChecker {
    /**
     * runTest.
     */
    public void runTest(long[] array) {
        long startTime = System.currentTimeMillis();
        boolean check = Arrays.stream(array)
                .parallel()
                .allMatch(IsPrime::isPrime);
        System.out.println("Время выполнения для Stream Parallel:"
                + (System.currentTimeMillis() - startTime));
    }
}
