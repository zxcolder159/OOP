package ru.nsu.ermakov.checkers;

import ru.nsu.ermakov.IsPrime;
import java.util.Arrays;

/**
 * Missing a JavaDoc comment.
 */
public class SimpleChecker {
    /**
     * Проверка без многопоточности.
     */
    public void runTest(long[] Array) {
        long startTime = System.currentTimeMillis();
        int size = Array.length;
        for (long x : Array){
            if (!IsPrime.isPrime(x)) break;

        }
        System.out.println("Время выполнения для однопоточного решения:" + (System.currentTimeMillis() - startTime));
    }
}
