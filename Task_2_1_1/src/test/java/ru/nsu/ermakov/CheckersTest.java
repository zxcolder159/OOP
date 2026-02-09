package ru.nsu.ermakov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.checkers.ParallelChecker;
import ru.nsu.ermakov.checkers.SimpleChecker;
import ru.nsu.ermakov.checkers.ThreadChecker;
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
        Assertions.assertDoesNotThrow(() -> checker.runTest(allPrimes));
        Assertions.assertDoesNotThrow(() -> checker.runTest(withComposite));
    }
    /**
     * Тест.
     */
    @Test
    void testParallelChecker() {
        ParallelChecker checker = new ParallelChecker();
        Assertions.assertDoesNotThrow(() -> checker.runTest(allPrimes));
        Assertions.assertDoesNotThrow(() -> checker.runTest(withComposite));
    }
    /**
     * Тест.
     */
    @Test
    void testThreadChecker() throws InterruptedException {
        ThreadChecker checker = new ThreadChecker();
        Assertions.assertDoesNotThrow(() -> checker.runTest(allPrimes, 1));
        Assertions.assertDoesNotThrow(() -> checker.runTest(withComposite, 4));
        Assertions.assertDoesNotThrow(() -> checker.runTest(new long[]{}, 2));
    }
}