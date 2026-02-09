package ru.nsu.ermakov.configs;

public class OrderConfig {
    public int orderSpawnRate;
    public int totalOrders;

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public OrderConfig() {}
    /**
     * Конструктор, как трек у Бабангиды.
     */
    public OrderConfig(int orderSpawnRate, int totalOrders) {
        this.orderSpawnRate = orderSpawnRate;
        this.totalOrders = totalOrders;
    }
}
