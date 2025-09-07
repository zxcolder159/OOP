import java.util.Arrays;

public class HeapsortTest {
    public static void main(String[] args) {
        // Test 1: Empty array
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{})), new int[]{});

        // Test 2: Single element
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{5})), new int[]{5});

        // Test 3: Already sorted
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{1, 2, 3, 4, 5})), new int[]{1, 2, 3, 4, 5});

        // Test 4: Reverse order
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{5, 4, 3, 2, 1})), new int[]{1, 2, 3, 4, 5});

        // Test 5: Random with duplicates
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{3, 1, 4, 1, 5, 9, 2})), new int[]{1, 1, 2, 3, 4, 5, 9});

        // Test 6: Negative numbers
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{-3, 5, -1, 0})), new int[]{-3, -1, 0, 5});

        // Test 7: All duplicates
        assert Arrays.equals(parse(Heapsort.heapsort(new int[]{7, 7, 7, 7})), new int[]{7, 7, 7, 7});

        System.out.println("All tests passed!");
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