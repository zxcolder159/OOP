package ru.nsu.ermakov.products;

/**
 * Интерфейс, создающий контракт для еды.
 */
public interface Food extends Product {
    /**
     * Геттер времени готовки.
     */
    long getCookingTime();
}
