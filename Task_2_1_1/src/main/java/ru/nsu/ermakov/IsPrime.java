package ru.nsu.ermakov;

/**
 * класс со статистическим методом для проверки на простату.
 */
public class IsPrime {
    /**
     * статистический метод для проверки на простату.
     * @param n
     * @return
     */
    public static boolean isPrime(long n) {
        if (n <= 1) { return false; }
        if (n <= 3) { return true; }
        if (n % 2 == 0 || n % 3 == 0) { return false; }

        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) { return false; }
        }
        return true;
    }
}
