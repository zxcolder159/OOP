package ru.nsu.ermakov.Checkers;

import ru.nsu.ermakov.IsPrime;

import java.util.Arrays;

public class ParallelChecker {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        int size = 1_500_000;
        long[] numbers = new long[size];
        Arrays.fill(numbers, 1000000007L);
        boolean check = Arrays.stream(numbers)
                .parallel()
                .allMatch(IsPrime::isPrime);
        System.out.println("Время выполнения:" + (System.currentTimeMillis() - startTime));
    }
}
