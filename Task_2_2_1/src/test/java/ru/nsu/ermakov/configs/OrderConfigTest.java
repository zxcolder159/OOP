package ru.nsu.ermakov.configs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;

/**
 * Тестовый класс для проверки конфигурации заказов.
 * Проверяет структуру данных конфигурации заказов.
 */
class OrderConfigTest {

    /**
     * Проверка создания OrderConfig по умолчанию.
     */
    @Test
    @DisplayName("Создание OrderConfig по умолчанию")
    void testDefaultConstructor() {
        OrderConfig config = new OrderConfig();
        assertNotNull(config);
        assertEquals(0, config.orderSpawnRate);
        assertEquals(0, config.totalOrders);
        assertNull(config.productsList);
    }

    /**
     * Проверка создания OrderConfig с параметрами.
     */
    @Test
    @DisplayName("Создание OrderConfig с параметрами")
    void testParameterizedConstructor() {
        OrderConfig config = new OrderConfig(1000, 50);
        assertNotNull(config);
        assertEquals(1000, config.orderSpawnRate);
        assertEquals(50, config.totalOrders);
        assertNull(config.productsList);
    }

    /**
     * Проверка установки и получения значений.
     */
    @Test
    @DisplayName("Установка и получение значений")
    void testSettersAndGetters() {
        OrderConfig config = new OrderConfig();
        config.orderSpawnRate = 2000;
        config.totalOrders = 100;
        config.productsList = List.of();
        assertEquals(2000, config.orderSpawnRate);
        assertEquals(100, config.totalOrders);
        assertNotNull(config.productsList);
        assertTrue(config.productsList.isEmpty());
    }

    /**
     * Проверка различных значений orderSpawnRate.
     */
    @Test
    @DisplayName("Различные значения orderSpawnRate")
    void testDifferentOrderSpawnRates() {
        OrderConfig config1 = new OrderConfig(500, 10);
        OrderConfig config2 = new OrderConfig(1000, 20);
        OrderConfig config3 = new OrderConfig(5000, 30);
        assertEquals(500, config1.orderSpawnRate);
        assertEquals(1000, config2.orderSpawnRate);
        assertEquals(5000, config3.orderSpawnRate);
        assertEquals(10, config1.totalOrders);
        assertEquals(20, config2.totalOrders);
        assertEquals(30, config3.totalOrders);
    }

    /**
     * Проверка нулевых и отрицательных значений.
     */
    @Test
    @DisplayName("Нулевые и отрицательные значения")
    void testZeroAndNegativeValues() {
        OrderConfig config1 = new OrderConfig(0, 0);
        OrderConfig config2 = new OrderConfig(-1, -1);
        assertEquals(0, config1.orderSpawnRate);
        assertEquals(0, config1.totalOrders);
        assertEquals(-1, config2.orderSpawnRate);
        assertEquals(-1, config2.totalOrders);
    }

    /**
     * Проверка больших значений.
     */
    @Test
    @DisplayName("Большие значения")
    void testLargeValues() {
        OrderConfig config = new OrderConfig(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, config.orderSpawnRate);
        assertEquals(Integer.MAX_VALUE, config.totalOrders);
    }
}
