package ru.nsu.ermakov.checkers;

import ru.nsu.ermakov.IsPrime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadChecker {
    private static volatile boolean foundComposite = false;
    public static void main (String[] args) throws InterruptedException {
       int size = 1_500_000;
       long[] numbers = new long[size];
       Arrays.fill(numbers, 1000000007L);
       int[] threadCounts = {2, 4, 8, 16};
       for(int count : threadCounts) {
           System.out.println(">>> Запуск на " + count + " сридах...");
           runTest(numbers, count);
       }
   }
   public static void runTest(long[] Array, int numThreads) throws InterruptedException {
       foundComposite = false;
       List<Thread> threads = new ArrayList<>();
       int chunkSize = Array.length / numThreads;
       long startTime = System.currentTimeMillis();
       for (int i = 0; i < numThreads; i++) {
           int startIdx = i * chunkSize;
           int endIdx = (i == numThreads - 1) ? Array.length : (i+1) * chunkSize;
           Thread t = new Thread(() -> {
               for(int j = startIdx; j < endIdx; j++) {
                   if (foundComposite) return;
                   if(!IsPrime.isPrime(Array[j])) {
                       foundComposite = true;
                       return;
                   }
               }
           });
           threads.add(t);
           t.start();
       }
       for(Thread t : threads) {
           t.join();
       }
       long duration = System.currentTimeMillis() - startTime;
       System.out.println("Результат: " +  numThreads + " сридов выполнил задачу за " + duration + "мс");
   }
}
