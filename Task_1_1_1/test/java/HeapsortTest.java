import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HeapsortTest {
    
    @Test
    void testEmptyArray() {
        assertArrayEquals(new int[]{}, parse(Heapsort.heapsort(new int[]{})));
    }

    @Test
    void testSingleElement() {
        assertArrayEquals(new int[]{5}, parse(Heapsort.heapsort(new int[]{5})));
    }

    @Test
    void testAlreadySorted() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parse(Heapsort.heapsort(new int[]{1, 2, 3, 4, 5})));
    }

    @Test
    void testReverseOrder() {
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, parse(Heapsort.heapsort(new int[]{5, 4, 3, 2, 1})));
    }

    @Test
    void testRandomWithDuplicates() {
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 9}, parse(Heapsort.heapsort(new int[]{3, 1, 4, 1, 5, 9, 2})));
    }

    @Test
    void testNegativeNumbers() {
        assertArrayEquals(new int[]{-3, -1, 0, 5}, parse(Heapsort.heapsort(new int[]{-3, 5, -1, 0})));
    }

    @Test
    void testAllDuplicates() {
        assertArrayEquals(new int[]{7, 7, 7, 7}, parse(Heapsort.heapsort(new int[]{7, 7, 7, 7})));
    }

    private static int[] parse(String s) {
        if (s.equals("[]")) return new int[0];
        String[] parts = s.replace("[", "").replace("]", "").split(", ");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++)
            arr[i] = Integer.parseInt(parts[i]);
        return arr;
    }
}
