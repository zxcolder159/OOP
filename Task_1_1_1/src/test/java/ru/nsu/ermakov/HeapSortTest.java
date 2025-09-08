package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class HeapSortTest {

    @Test
    public void testEmptyArray() {
        int[] result = HeapSort.heapsortForTest(new int[]{});
        assertArrayEquals(new int[]{}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{});
        assertEquals("[]", stringResult);
    }

    @Test
    public void testSingleElement() {
        int[] result = HeapSort.heapsortForTest(new int[]{5});
        assertArrayEquals(new int[]{5}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{5});
        assertEquals("[5]", stringResult);
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

    @Test
    public void testNullArray() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            HeapSort.heapsortForTest(null);
        });
        assertEquals("Массив не может быть null", exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            HeapSort.heapsort(null);
        });
        assertEquals("Массив не может быть null", exception.getMessage());
    }

    @Test
    public void testLargeNumbers() {
        int[] result = HeapSort.heapsortForTest(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0});
        assertArrayEquals(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, result);
    }

    @Test
    public void testMixedPositiveNegative() {
        int[] result = HeapSort.heapsortForTest(new int[]{-5, 10, -3, 7, 0, -1});
        assertArrayEquals(new int[]{-5, -3, -1, 0, 7, 10}, result);
    }

    @Test
    public void testStringOutputFormat() {
        String result = HeapSort.heapsort(new int[]{1, 2, 3});
        assertEquals("[1, 2, 3]", result);
        
        result = HeapSort.heapsort(new int[]{1});
        assertEquals("[1]", result);
        
        result = HeapSort.heapsort(new int[]{1, 2});
        assertEquals("[1, 2]", result);
    }

    @Test
    public void testZeroElements() {
        int[] result = HeapSort.heapsortForTest(new int[]{0, 0, 0});
        assertArrayEquals(new int[]{0, 0, 0}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{0, 0, 0});
        assertEquals("[0, 0, 0]", stringResult);
    }

    @Test
    public void testPerformanceWithLargeArray() {
        int[] largeArray = new int[1000];
        for (int i = 0; i < 1000; i++) {
            largeArray[i] = 999 - i; // обратный порядок
        }
        
        int[] result = HeapSort.heapsortForTest(largeArray);
        assertEquals(0, result[0]);
        assertEquals(999, result[999]);
        assertEquals(500, result[500]);
    }
}
