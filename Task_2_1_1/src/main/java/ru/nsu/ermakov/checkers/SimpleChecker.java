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
    public static void main (String[] args) {
        long startTime = System.currentTimeMillis();
        int size = 1_500_000;
        long[] numbers = new long[size];
        Arrays.fill(numbers, 1000000007L);
        for(long x : numbers){
            if(!IsPrime.isPrime(x)) break;

        }
        System.out.println("Время выполнения:" + (System.currentTimeMillis() - startTime));
    }
}
