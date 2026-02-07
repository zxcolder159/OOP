package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Тест.
 */
class IsPrimeTest {
    /**
     * Тест.
     */
    @Test
    void testIsPrime() {
        assertFalse(IsPrime.isPrime(-5));
        assertFalse(IsPrime.isPrime(0));
        assertFalse(IsPrime.isPrime(1));

        assertTrue(IsPrime.isPrime(2));
        assertTrue(IsPrime.isPrime(3));
        assertTrue(IsPrime.isPrime(5));

        assertFalse(IsPrime.isPrime(4));
        assertFalse(IsPrime.isPrime(9));
        assertFalse(IsPrime.isPrime(25));

        assertTrue(IsPrime.isPrime(104729));
        assertFalse(IsPrime.isPrime(121)); // 11 * 11
    }
}