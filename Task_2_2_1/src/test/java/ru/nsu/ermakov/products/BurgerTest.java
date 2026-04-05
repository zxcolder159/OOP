package ru.nsu.ermakov.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки функциональности бургера.
 * Проверяет специфичные для бургера методы и поведение.
 */
class BurgerTest {

    /**
     * Проверка создания бургера.
     */
    @Test
    @DisplayName("Создание бургера")
    void testBurgerCreation() {
        Burger burger = new Burger(1);
        
        assertEquals(1, burger.getId());
        assertEquals(1, burger.getSize());
        assertEquals(0, burger.getOrderId());
        assertEquals(1000, burger.getCookingTime());
    }

    /**
     * Проверка времени готовки бургера.
     */
    @Test
    @DisplayName("Время готовки бургера")
    void testBurgerCookingTime() {
        Burger burger1 = new Burger(1);
        Burger burger2 = new Burger(2);
        
        assertEquals(1000, burger1.getCookingTime());
        assertEquals(1000, burger2.getCookingTime());
    }

    /**
     * Проверка размера бургера.
     */
    @Test
    @DisplayName("Размер бургера")
    void testBurgerSize() {
        Burger burger = new Burger(5);
        
        assertEquals(1, burger.getSize());
    }

    /**
     * Проверка установки и получения orderId.
     */
    @Test
    @DisplayName("Установка и получение orderId")
    void testBurgerOrderId() {
        Burger burger = new Burger(10);
        
        assertEquals(0, burger.getOrderId());
        
        burger.setOrderId(123);
        assertEquals(123, burger.getOrderId());
        
        burger.setOrderId(456);
        assertEquals(456, burger.getOrderId());
    }

    /**
     * Проверка клонирования бургера.
     */
    @Test
    @DisplayName("Клонирование бургера")
    void testBurgerClone() {
        Burger original = new Burger(99);
        original.setOrderId(999);
        
        Burger cloned = (Burger) original.clone();
        
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
    void testBurgerImplementsFood() {
        Burger burger = new Burger(7);
        
        assertTrue(burger instanceof Food);
        assertTrue(burger instanceof Product);
        
        Food foodInterface = burger;
        assertEquals(1000, foodInterface.getCookingTime());
        
        Product productInterface = burger;
        assertEquals(7, productInterface.getId());
        assertEquals(1, productInterface.getSize());
    }

    /**
     * Проверка создания бургера с разными ID.
     */
    @Test
    @DisplayName("Создание бургера с разными ID")
    void testBurgerWithDifferentIds() {
        Burger burger1 = new Burger(1);
        Burger burger2 = new Burger(100);
        Burger burger3 = new Burger(-5);
        
        assertEquals(1, burger1.getId());
        assertEquals(100, burger2.getId());
        assertEquals(-5, burger3.getId());
        
        assertEquals(1, burger1.getSize());
        assertEquals(1, burger2.getSize());
        assertEquals(1, burger3.getSize());
    }

    /**
     * Проверка неизменности полей бургера.
     */
    @Test
    @DisplayName("Неизменность полей бургера")
    void testBurgerImmutability() {
        Burger burger = new Burger(42);
        final long initialCookingTime = burger.getCookingTime();
        int initialId = burger.getId();
        int initialSize = burger.getSize();
        burger.setOrderId(777);
        assertEquals(initialId, burger.getId());
        assertEquals(initialSize, burger.getSize());
        assertEquals(initialCookingTime, burger.getCookingTime());
        assertEquals(777, burger.getOrderId());
    }

    /**
     * Проверка сравнения времени готовки с пиццей.
     */
    @Test
    @DisplayName("Сравнение времени готовки с пиццей")
    void testCookingTimeComparisonWithPizza() {
        Burger burger = new Burger(1);
        Pizza pizza = new Pizza(2);
        
        assertTrue(burger.getCookingTime() < pizza.getCookingTime());
        assertEquals(1000, burger.getCookingTime());
        assertEquals(2000, pizza.getCookingTime());
    }
}
