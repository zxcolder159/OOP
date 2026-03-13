package ru.nsu.ermakov.configs;

/**
 * Конфиг для работы с хранилицем.
 */
public class WarehouseConfig {
    public int storageSize;

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public WarehouseConfig() {

    }

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public WarehouseConfig(int storageSize) {
        this.storageSize = storageSize;
    }
}
