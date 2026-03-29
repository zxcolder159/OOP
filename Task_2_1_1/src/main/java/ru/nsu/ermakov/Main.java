package ru.nsu.ermakov;

import ru.nsu.ermakov.checkers.ParallelChecker;
import ru.nsu.ermakov.checkers.SimpleChecker;
import ru.nsu.ermakov.checkers.ThreadChecker;
import java.util.Arrays;

/**
 * Main.
 */
public class Main {
    /**
     * Main.
     */
    public static void main (String[] args) throws InterruptedException {
        int size = 1_500_000;
        long[] numbers = new long[size];
        Arrays.fill(numbers, 1000000007L);
        int[] threadCounts = {2, 4, 8, 16};
        SimpleChecker simpleChecker = new SimpleChecker();
        simpleChecker.runTest(numbers);

        for (int count : threadCounts) {
            System.out.println(">>> Запуск на " + count + " сридах...");
            ThreadChecker threadChecker = new ThreadChecker();
            threadChecker.runTest(numbers, count);
        }

        ParallelChecker parallelChecker = new ParallelChecker();
        parallelChecker.runTest(numbers);
    }
}
