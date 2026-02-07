package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.checkers.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Тест.
 */
class CheckersTest {
    private final long[] allPrimes = {2, 3, 5, 7, 11, 13, 17, 19};
    private final long[] withComposite = {2, 3, 4, 7, 11};
    /**
     * Тест.
     */
    @Test
    void testSimpleChecker() {
        SimpleChecker checker = new SimpleChecker();
        assertDoesNotThrow(() -> checker.runTest(allPrimes));
        assertDoesNotThrow(() -> checker.runTest(withComposite));
    }
    /**
     * Тест.
     */
    @Test
    void testParallelChecker() {
        ParallelChecker checker = new ParallelChecker();
        assertDoesNotThrow(() -> checker.runTest(allPrimes));
        assertDoesNotThrow(() -> checker.runTest(withComposite));
    }
    /**
     * Тест.
     */
    @Test
    void testThreadChecker() throws InterruptedException {
        ThreadChecker checker = new ThreadChecker();
        // Тест с разным количеством потоков
        assertDoesNotThrow(() -> checker.runTest(allPrimes, 1));
        assertDoesNotThrow(() -> checker.runTest(withComposite, 4));

        // Тест на пустом массиве (краевой случай)
        assertDoesNotThrow(() -> checker.runTest(new long[]{}, 2));
    }
}