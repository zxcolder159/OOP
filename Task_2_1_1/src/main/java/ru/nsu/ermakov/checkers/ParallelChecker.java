package ru.nsu.ermakov.checkers;

import ru.nsu.ermakov.IsPrime;
import java.util.Arrays;

public class ParallelChecker {
    public void runTest(long[] Array) {
        long startTime = System.currentTimeMillis();
        boolean check = Arrays.stream(Array)
                .parallel()
                .allMatch(IsPrime::isPrime);
        System.out.println("Время выполнения для Stream Parallel:" + (System.currentTimeMillis() - startTime));
    }
}
