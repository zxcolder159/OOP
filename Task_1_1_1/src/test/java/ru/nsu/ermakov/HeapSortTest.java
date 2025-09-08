package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class HeapSortTest {

    @Test
    public void testEmptyArray() {
        int[] result = HeapSort.heapsortForTest(new int[]{});
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    public void testSingleElement() {
        int[] result = HeapSort.heapsortForTest(new int[]{5});
        assertArrayEquals(new int[]{5}, result);
    }

    @Test
    public void testAlreadySorted() {
        int[] result = HeapSort.heapsortForTest(new int[]{1, 2, 3, 4, 5});
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testReverseOrder() {
        int[] result = HeapSort.heapsortForTest(new int[]{5, 4, 3, 2, 1});
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testRandomWithDuplicates() {
        int[] result = HeapSort.heapsortForTest(new int[]{3, 1, 4, 1, 5, 9, 2});
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 9}, result);
    }

    @Test
    public void testNegativeNumbers() {
        int[] result = HeapSort.heapsortForTest(new int[]{-3, 5, -1, 0});
        assertArrayEquals(new int[]{-3, -1, 0, 5}, result);
    }

    @Test
    public void testAllDuplicates() {
        int[] result = HeapSort.heapsortForTest(new int[]{7, 7, 7, 7});
        assertArrayEquals(new int[]{7, 7, 7, 7}, result);
    }
}