package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;

public class HeapSortTest {

    @Test
    public void testEmptyArray() {
        int[] result = HeapSort.heapsortForTest(new int[]{});
        assertArrayEquals(new int[]{}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{});
        assertEquals("[]", stringResult);
        
        int[] emptyArray = new int[]{};
        HeapSort.sort(emptyArray);
        assertArrayEquals(new int[]{}, emptyArray);
    }

    @Test
    public void testSingleElement() {
        int[] result = HeapSort.heapsortForTest(new int[]{5});
        assertArrayEquals(new int[]{5}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{5});
        assertEquals("[5]", stringResult);
        
        int[] singleArray = new int[]{5};
        HeapSort.sort(singleArray);
        assertArrayEquals(new int[]{5}, singleArray);
    }

    @Test
    public void testAlreadySorted() {
        int[] input = {1, 2, 3, 4, 5};
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
        
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, input);
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
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            HeapSort.sort(null);
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
        
        result = HeapSort.heapsort(new int[]{});
        assertEquals("[]", result);
    }

    @Test
    public void testZeroElements() {
        int[] result = HeapSort.heapsortForTest(new int[]{0, 0, 0});
        assertArrayEquals(new int[]{0, 0, 0}, result);
        
        String stringResult = HeapSort.heapsort(new int[]{0, 0, 0});
        assertEquals("[0, 0, 0]", stringResult);
    }

    @Test
    public void testSortModifiesOriginal() {
        int[] original = {5, 3, 8, 1};
        int[] copy = original.clone();
        HeapSort.sort(original);
        assertFalse(java.util.Arrays.equals(original, copy));
        assertArrayEquals(new int[]{1, 3, 5, 8}, original);
    }

    @Test
    public void testArrayToStringEdgeCases() {
        String result = HeapSort.heapsort(new int[]{});
        assertEquals("[]", result);
        
        result = HeapSort.heapsort(new int[]{42});
        assertEquals("[42]", result);
        
        result = HeapSort.heapsort(new int[]{1, 2});
        assertEquals("[1, 2]", result);
    }

    @Test
    public void testHeapSortDoesNotModifyInput() {
        int[] input = {5, 3, 8, 1};
        int[] original = input.clone();
        HeapSort.heapsortForTest(input);
        assertArrayEquals(original, input); 
        
        String result = HeapSort.heapsort(input);
        assertArrayEquals(original, input); 
    }

    @Test
    public void testLargeArray() {
        int[] largeArray = new int[1000];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = (int) (Math.random() * 1000);
        }
        
        int[] sorted = largeArray.clone();
        Arrays.sort(sorted);
        
        int[] result = HeapSort.heapsortForTest(largeArray);
        assertArrayEquals(sorted, result);
    }

    @Test
    public void testAlreadyMaxHeap() {
        int[] maxHeap = {10, 8, 9, 5, 7, 6, 3};
        int[] expected = {3, 5, 6, 7, 8, 9, 10};
        
        int[] result = HeapSort.heapsortForTest(maxHeap);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAlreadyMinHeap() {
        int[] minHeap = {1, 2, 3, 4, 5, 6, 7};
        int[] expected = {1, 2, 3, 4, 5, 6, 7};
        
        int[] result = HeapSort.heapsortForTest(minHeap);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testAlternatingNumbers() {
        int[] input = {1, -1, 2, -2, 3, -3, 4, -4};
        int[] expected = {-4, -3, -2, -1, 1, 2, 3, 4};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBoundaryValues() {
        int[] input = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1};
        int[] expected = {Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testPerformanceWithDuplicates() {
        int[] input = new int[500];
        for (int i = 0; i < input.length; i++) {
            input[i] = i % 10;
        }
        
        int[] sorted = input.clone();
        Arrays.sort(sorted);
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(sorted, result);
    }

    @Test
    public void testDescendingOrder() {
        int[] input = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSingleNegative() {
        int[] input = {-5};
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(new int[]{-5}, result);
        
        String stringResult = HeapSort.heapsort(input);
        assertEquals("[-5]", stringResult);
    }

    @Test
    public void testAllNegative() {
        int[] input = {-5, -10, -3, -8, -1};
        int[] expected = {-10, -8, -5, -3, -1};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMixedWithExtremes() {
        int[] input = {Integer.MAX_VALUE, 0, Integer.MIN_VALUE, -1000000, 1000000};
        int[] expected = {Integer.MIN_VALUE, -1000000, 0, 1000000, Integer.MAX_VALUE};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testStringOutputWithNegative() {
        String result = HeapSort.heapsort(new int[]{-3, -1, 0, 2});
        assertEquals("[-3, -1, 0, 2]", result);
    }

    @Test
    public void testStringOutputWithSingleNegative() {
        String result = HeapSort.heapsort(new int[]{-42});
        assertEquals("[-42]", result);
    }

    @Test
    public void testHeapifyProcess() {
        int[] input = {4, 10, 3, 5, 1};
        int[] expected = {1, 3, 4, 5, 10};
        
        int[] result = HeapSort.heapsortForTest(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMultipleCalls() {
        int[] input1 = {5, 3, 8, 1};
        int[] input2 = {5, 3, 8, 1};
        
        int[] result1 = HeapSort.heapsortForTest(input1);
        int[] result2 = HeapSort.heapsortForTest(input2);
        
        assertArrayEquals(result1, result2);
        assertArrayEquals(input1, input2);
    }
}
