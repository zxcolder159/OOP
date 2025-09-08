package ru.nsu.masolygin;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class HeapsortTest {

    @Test
    void emptyArrayTest() {
        int[] arr = {};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void singleElementTest() {
        int[] arr = {42};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void alreadySortedTest() {
        int[] arr = {1, 2, 3, 4, 5};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void reverseSortedTest() {
        int[] arr = {5, 4, 3, 2, 1};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void duplicatesTest() {
        int[] arr = {5, 1, 5, 3, 1, 2};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 5, 5}, arr);
    }

    @Test
    void allSameElementsTest() {
        int[] arr = {7, 7, 7, 7, 7};
        Heapsort.heapsort(arr);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
    }

    @Test
    void negativeNumbersTest() {
        int[] arr = {-5, -1, -3, -2, -10};
        int[] copy = arr.clone();
        Arrays.sort(copy);

        Heapsort.heapsort(arr);
        assertArrayEquals(copy, arr);
    }

    @Test
    void mixedPositiveNegativeTest() {
        int[] arr = {-2, 0, 5, -1, 3, -10, 2};
        int[] copy = arr.clone();
        Arrays.sort(copy);

        Heapsort.heapsort(arr);
        assertArrayEquals(copy, arr);
    }

    @Test
    void extremeValuesTest() {
        int[] arr = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, 42};
        int[] copy = arr.clone();
        Arrays.sort(copy);

        Heapsort.heapsort(arr);
        assertArrayEquals(copy, arr);
    }

    @Test
    void randomArrayTest() {
        int[] arr = {10, -3, 7, 0, 2, -10, 5};
        int[] copy = arr.clone();
        Arrays.sort(copy);

        Heapsort.heapsort(arr);
        assertArrayEquals(copy, arr);
    }

    @Test
    void largeArrayStressTest() {
        Random random = new Random(0);
        int[] arr = new int[10000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt();
        }
        int[] copy = arr.clone();
        Arrays.sort(copy);

        Heapsort.heapsort(arr);
        assertArrayEquals(copy, arr);
    }

    @Test
    void shiftDownTestNoChange() {
        int[] arr = {5, 3, 4};
        Heapsort.shiftDown(arr, arr.length, 0);
        assertArrayEquals(new int[]{5, 3, 4}, arr);
    }

    @Test
    void shiftDownTestSwapLeft() {
        int[] arr = {3, 5, 4};
        Heapsort.shiftDown(arr, arr.length, 0);
        assertArrayEquals(new int[]{5, 3, 4}, arr);
    }

    @Test
    void shiftDownTestSwapRight() {
        int[] arr = {3, 4, 5};
        Heapsort.shiftDown(arr, arr.length, 0);
        assertArrayEquals(new int[]{5, 4, 3}, arr);
    }

    @Test
    void shiftDownTestNoChildren() {
        int[] arr = {5};
        Heapsort.shiftDown(arr, arr.length, 0);
        assertArrayEquals(new int[]{5}, arr);
    }

    @Test
    void swapTest() {
        int[] arr = {1, 2};
        Heapsort.swap(arr, 0, 1);
        assertArrayEquals(new int[]{2, 1}, arr);
    }

    @Test
    void timeTest() {
        int[] sizes = {1000, 5000, 10000, 20000, 50000, 100000, 200000};
        double[] times = new double[sizes.length];
        double[] theoreticalTimes = new double[sizes.length];


        for (int i = 0; i < sizes.length; i++) {
            int size = sizes[i];
            Random random = new Random(0);

            int[] arr = new int[size];
            for (int j = 0; j < size; j++) {
                arr[j] = random.nextInt();
            }

            long startTime = System.nanoTime();
            Heapsort.heapsort(arr);
            long endTime = System.nanoTime();

            long duration = endTime - startTime;
            double durationInMillis = duration / 1_000_000.0;
            times[i] = durationInMillis;

            theoreticalTimes[i] = size * Math.log(size) / Math.log(2);
        }

        for (int i = 1; i < times.length; i++) {


            double expectGrowth = theoreticalTimes[i] / theoreticalTimes[i - 1];
            double realGrowth = times[i] / times[i - 1];

            assertTrue(realGrowth < expectGrowth * 1.2);
        }
    }
}
