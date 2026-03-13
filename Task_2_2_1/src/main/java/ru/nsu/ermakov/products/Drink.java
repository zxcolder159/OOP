package ru.nsu.ermakov.products;

/**
 * Интерфейс, создающий контракт для напитков.
 */
public interface Drink extends Product {

    /**
     * Геттер времени подготовки.
     */
    long getProcessingTime();
}
