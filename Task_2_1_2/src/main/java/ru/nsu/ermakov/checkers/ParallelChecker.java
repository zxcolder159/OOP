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
    public boolean runTest(long[] array) {
        boolean allPrime = Arrays.stream(array)
                .parallel()
                .allMatch(IsPrime::isPrime);
        return allPrime;
    }
}
