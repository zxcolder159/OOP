package ru.nsu.ermakov.products;

/**
 * Интерфейс для продуктов.
 * В лабе есть только 1 вид пиццы, но этот интерфейс добавит расширяемость кода, и вообще в стилистике ООП.
 */
public interface Product {
    /**
     * Геттер размера.
     */
    public int getSize();
    /**
     * Геттер id.
     */
    public int getId();


}
