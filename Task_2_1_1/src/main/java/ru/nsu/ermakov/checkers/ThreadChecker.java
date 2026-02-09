package ru.nsu.ermakov.checkers;

import ru.nsu.ermakov.IsPrime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Сридчекер.
 */
public class ThreadChecker {
    private volatile boolean foundComposite = false;

    /**
     * runTest.
     */
    public boolean runTest(long[] array, int numThreads) throws InterruptedException {
        foundComposite = false;
        List<Thread> threads = new ArrayList<>();
        int chunkSize = array.length / numThreads;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            int startIdx = i * chunkSize;
            int endIdx = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;
            Thread t = new Thread(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    if (foundComposite) {
                        return;
                    }
                    if (!IsPrime.isPrime(array[j])) {
                        foundComposite = true;
                        return;
                    }
                }
            });
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Результат: " +  numThreads +
                " сридов выполнил задачу за " + duration + "мс");
        return !foundComposite;
    }
}
