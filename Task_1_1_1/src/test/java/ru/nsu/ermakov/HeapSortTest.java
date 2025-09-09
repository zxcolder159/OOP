package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;

public class HeapSortTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

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
    public void testMainWithValidInput() {
        String input = "5 3 8 1 4\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Отсортированный массив:"));
        assertTrue(output.contains("[1, 3, 4, 5, 8]"));
    }

    @Test
    public void testMainWithEmptyInput() {
        String input = "\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Вы не ввели числа!"));
    }

    @Test
    public void testMainWithInvalidInput() {
        String input = "5 abc 3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Ошибка: введите только целые числа!"));
    }

    @Test
    public void testMainWithSingleNumber() {
        String input = "42\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Отсортированный массив:"));
        assertTrue(output.contains("[42]"));
    }

    @Test
    public void testMainWithNegativeNumbers() {
        String input = "-5 3 -1 0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Отсортированный массив:"));
        assertTrue(output.contains("[-5, -1, 0, 3]"));
    }

    @Test
    public void testMainWithMultipleSpaces() {
        String input = "   5   3   8   1   \n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        HeapSort.main(new String[]{});
        
        String output = outContent.toString();
        assertTrue(output.contains("Отсортированный массив:"));
        assertTrue(output.contains("[1, 3, 5, 8]"));
    }

    @Test
    public void testBuildMaxHeapIndirectly() {
        int[] array = {3, 1, 4};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{1, 3, 4}, array);
    }

    @Test
    public void testSwapLogicThroughIntegration() {
        int[] array = {5, 3};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{3, 5}, array);
    }
}
