package ru.nsu.ermakov.configs;

public class OrderConfig {
    int orderSpawnRate;
    int totalOrders;

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public OrderConfig(int orderSpawnRate, int totalOrders) {
        this.orderSpawnRate = orderSpawnRate;
        this.totalOrders = totalOrders;
    }
}
