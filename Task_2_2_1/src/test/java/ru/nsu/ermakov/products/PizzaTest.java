package ru.nsu.ermakov.products;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Тестовый класс для проверки функциональности пиццы.
 * Проверяет специфичные для пиццы методы и поведение.
 */
class PizzaTest {

    /**
     * Проверка создания пиццы.
     */
    @Test
    @DisplayName("Создание пиццы")
    void testPizzaCreation() {
        Pizza pizza = new Pizza(1);
        
        assertEquals(1, pizza.getId());
        assertEquals(2, pizza.getSize());
        assertEquals(0, pizza.getOrderId());
        assertEquals(2000, pizza.getCookingTime());
    }

    /**
     * Проверка времени готовки пиццы.
     */
    @Test
    @DisplayName("Время готовки пиццы")
    void testPizzaCookingTime() {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        
        assertEquals(2000, pizza1.getCookingTime());
        assertEquals(2000, pizza2.getCookingTime());
    }

    /**
     * Проверка размера пиццы.
     */
    @Test
    @DisplayName("Размер пиццы")
    void testPizzaSize() {
        Pizza pizza = new Pizza(5);
        
        assertEquals(2, pizza.getSize());
    }

    /**
     * Проверка установки и получения orderId.
     */
    @Test
    @DisplayName("Установка и получение orderId")
    void testPizzaOrderId() {
        Pizza pizza = new Pizza(10);
        
        assertEquals(0, pizza.getOrderId());
        
        pizza.setOrderId(123);
        assertEquals(123, pizza.getOrderId());
        
        pizza.setOrderId(456);
        assertEquals(456, pizza.getOrderId());
    }

    /**
     * Проверка клонирования пиццы.
     */
    @Test
    @DisplayName("Клонирование пиццы")
    void testPizzaClone() {
        Pizza original = new Pizza(99);
        original.setOrderId(999);
        
        Pizza cloned = (Pizza) original.clone();
        
        assertNotSame(original, cloned);
        assertEquals(original.getId(), cloned.getId());
        assertEquals(original.getSize(), cloned.getSize());
        assertEquals(original.getCookingTime(), cloned.getCookingTime());
        assertEquals(0, cloned.getOrderId());
    }

    /**
     * Проверка реализации интерфейса Food.
     */
    @Test
    @DisplayName("Реализация интерфейса Food")
    void testPizzaImplementsFood() {
        Pizza pizza = new Pizza(7);
        
        assertTrue(pizza instanceof Food);
        assertTrue(pizza instanceof Product);
        
        Food foodInterface = pizza;
        assertEquals(2000, foodInterface.getCookingTime());
        
        Product productInterface = pizza;
        assertEquals(7, productInterface.getId());
        assertEquals(2, productInterface.getSize());
    }

    /**
     * Проверка создания пиццы с разными ID.
     */
    @Test
    @DisplayName("Создание пиццы с разными ID")
    void testPizzaWithDifferentIds() {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(100);
        Pizza pizza3 = new Pizza(-5);
        
        assertEquals(1, pizza1.getId());
        assertEquals(100, pizza2.getId());
        assertEquals(-5, pizza3.getId());
        
        assertEquals(2, pizza1.getSize());
        assertEquals(2, pizza2.getSize());
        assertEquals(2, pizza3.getSize());
    }

    /**
     * Проверка неизменности полей пиццы.
     */
    @Test
    @DisplayName("Неизменность полей пиццы")
    void testPizzaImmutability() {
        Pizza pizza = new Pizza(1);
        final long initialCookingTime = pizza.getCookingTime();
        int initialId = pizza.getId();
        int initialSize = pizza.getSize();
        pizza.setOrderId(777);
        assertEquals(initialId, pizza.getId());
        assertEquals(initialSize, pizza.getSize());
        assertEquals(initialCookingTime, pizza.getCookingTime());
        assertEquals(777, pizza.getOrderId());
    }
}
