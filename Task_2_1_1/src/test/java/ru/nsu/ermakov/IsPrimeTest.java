package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


/**
 * Тест.
 */
class IsPrimeTest {
    /**
     * Тест.
     */
    @Test
    void testIsPrime() {
        Assertions.assertFalse(IsPrime.isPrime(-5));
        Assertions.assertFalse(IsPrime.isPrime(0));
        Assertions.assertFalse(IsPrime.isPrime(1));

        Assertions.assertTrue(IsPrime.isPrime(2));
        Assertions.assertTrue(IsPrime.isPrime(3));
        Assertions.assertTrue(IsPrime.isPrime(5));

        Assertions.assertFalse(IsPrime.isPrime(4));
        Assertions.assertFalse(IsPrime.isPrime(9));

        Assertions.assertTrue(IsPrime.isPrime(104729));
        Assertions.assertFalse(IsPrime.isPrime(121));
    }
}