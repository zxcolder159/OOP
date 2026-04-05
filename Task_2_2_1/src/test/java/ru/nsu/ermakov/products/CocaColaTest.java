package ru.nsu.ermakov.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки функциональности Coca-Cola.
 * Проверяет специфичные для напитка методы и поведение.
 */
class CocaColaTest {

    /**
     * Проверка создания Coca-Cola.
     */
    @Test
    @DisplayName("Создание Coca-Cola")
    void testCocaColaCreation() {
        CocaCola cola = new CocaCola(1);
        
        assertEquals(1, cola.getId());
        assertEquals(1, cola.getSize());
        assertEquals(0, cola.getOrderId());
        assertEquals(0, cola.getProcessingTime());
    }

    /**
     * Проверка времени подготовки Coca-Cola.
     */
    @Test
    @DisplayName("Время подготовки Coca-Cola")
    void testCocaColaProcessingTime() {
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        
        assertEquals(0, cola1.getProcessingTime());
        assertEquals(0, cola2.getProcessingTime());
    }

    /**
     * Проверка размера Coca-Cola.
     */
    @Test
    @DisplayName("Размер Coca-Cola")
    void testCocaColaSize() {
        CocaCola cola = new CocaCola(5);
        
        assertEquals(1, cola.getSize());
    }

    /**
     * Проверка установки и получения orderId.
     */
    @Test
    @DisplayName("Установка и получение orderId")
    void testCocaColaOrderId() {
        CocaCola cola = new CocaCola(10);
        
        assertEquals(0, cola.getOrderId());
        
        cola.setOrderId(123);
        assertEquals(123, cola.getOrderId());
        
        cola.setOrderId(456);
        assertEquals(456, cola.getOrderId());
    }

    /**
     * Проверка клонирования Coca-Cola.
     */
    @Test
    @DisplayName("Клонирование Coca-Cola")
    void testCocaColaClone() {
        CocaCola original = new CocaCola(99);
        original.setOrderId(999);
        
        CocaCola cloned = (CocaCola) original.clone();
        
        assertNotSame(original, cloned);
        assertEquals(original.getId(), cloned.getId());
        assertEquals(original.getSize(), cloned.getSize());
        assertEquals(original.getProcessingTime(), cloned.getProcessingTime());
        assertEquals(0, cloned.getOrderId());
    }

    /**
     * Проверка реализации интерфейса Drink.
     */
    @Test
    @DisplayName("Реализация интерфейса Drink")
    void testCocaColaImplementsDrink() {
        CocaCola cola = new CocaCola(7);
        
        assertTrue(cola instanceof Drink);
        assertTrue(cola instanceof Product);
        
        Drink drinkInterface = cola;
        assertEquals(0, drinkInterface.getProcessingTime());
        
        Product productInterface = cola;
        assertEquals(7, productInterface.getId());
        assertEquals(1, productInterface.getSize());
    }

    /**
     * Проверка создания Coca-Cola с разными ID.
     */
    @Test
    @DisplayName("Создание Coca-Cola с разными ID")
    void testCocaColaWithDifferentIds() {
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(100);
        CocaCola cola3 = new CocaCola(-5);
        
        assertEquals(1, cola1.getId());
        assertEquals(100, cola2.getId());
        assertEquals(-5, cola3.getId());
        
        assertEquals(1, cola1.getSize());
        assertEquals(1, cola2.getSize());
        assertEquals(1, cola3.getSize());
    }

    /**
     * Проверка неизменности полей Coca-Cola.
     */
    @Test
    @DisplayName("Неизменность полей Coca-Cola")
    void testCocaColaImmutability() {
        CocaCola cola = new CocaCola(42);
        final long initialProcessingTime = cola.getProcessingTime();
        int initialId = cola.getId();
        int initialSize = cola.getSize();
        cola.setOrderId(777);
        assertEquals(initialId, cola.getId());
        assertEquals(initialSize, cola.getSize());
        assertEquals(initialProcessingTime, cola.getProcessingTime());
        assertEquals(777, cola.getOrderId());
    }

    /**
     * Проверка быстрой обработки напитка.
     */
    @Test
    @DisplayName("Быстрая обработка напитка")
    void testFastProcessing() {
        CocaCola cola = new CocaCola(1);
        
        assertEquals(0, cola.getProcessingTime());
        
        cola.setOrderId(500);
        
        assertEquals(0, cola.getProcessingTime());
        assertEquals(500, cola.getOrderId());
    }
}
