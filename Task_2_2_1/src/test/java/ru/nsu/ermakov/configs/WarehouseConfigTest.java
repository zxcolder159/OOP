package ru.nsu.ermakov.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тестовый класс для проверки конфигурации склада.
 * Проверяет структуру данных конфигурации склада.
 */
class WarehouseConfigTest {

    /**
     * Проверка создания WarehouseConfig по умолчанию.
     */
    @Test
    @DisplayName("Создание WarehouseConfig по умолчанию")
    void testDefaultConstructor() {
        WarehouseConfig config = new WarehouseConfig();
        assertNotNull(config);
        assertEquals(0, config.storageSize);
    }

    /**
     * Проверка создания WarehouseConfig с параметром.
     */
    @Test
    @DisplayName("Создание WarehouseConfig с параметром")
    void testParameterizedConstructor() {
        WarehouseConfig config = new WarehouseConfig(100);
        assertNotNull(config);
        assertEquals(100, config.storageSize);
    }

    /**
     * Проверка установки и получения значения storageSize.
     */
    @Test
    @DisplayName("Установка и получение storageSize")
    void testStorageSizeSetterGetter() {
        WarehouseConfig config = new WarehouseConfig();
        config.storageSize = 50;
        assertEquals(50, config.storageSize);
        config.storageSize = 200;
        assertEquals(200, config.storageSize);
    }

    /**
     * Проверка различных значений storageSize.
     */
    @Test
    @DisplayName("Различные значения storageSize")
    void testDifferentStorageSizes() {
        WarehouseConfig config1 = new WarehouseConfig(10);
        WarehouseConfig config2 = new WarehouseConfig(50);
        WarehouseConfig config3 = new WarehouseConfig(1000);
        assertEquals(10, config1.storageSize);
        assertEquals(50, config2.storageSize);
        assertEquals(1000, config3.storageSize);
    }

    /**
     * Проверка нулевого значения storageSize.
     */
    @Test
    @DisplayName("Нулевое значение storageSize")
    void testZeroStorageSize() {
        WarehouseConfig config = new WarehouseConfig(0);
        assertEquals(0, config.storageSize);
    }

    /**
     * Проверка отрицательного значения storageSize.
     */
    @Test
    @DisplayName("Отрицательное значение storageSize")
    void testNegativeStorageSize() {
        WarehouseConfig config = new WarehouseConfig(-10);
        assertEquals(-10, config.storageSize);
    }

    /**
     * Проверка максимального значения storageSize.
     */
    @Test
    @DisplayName("Максимальное значение storageSize")
    void testMaxStorageSize() {
        WarehouseConfig config = new WarehouseConfig(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, config.storageSize);
    }

    /**
     * Проверка изменения storageSize после создания.
     */
    @Test
    @DisplayName("Изменение storageSize после создания")
    void testModifyStorageSizeAfterCreation() {
        WarehouseConfig config = new WarehouseConfig(100);
        assertEquals(100, config.storageSize);
        config.storageSize = 150;
        assertEquals(150, config.storageSize);
        config.storageSize = 0;
        assertEquals(0, config.storageSize);
    }
}
