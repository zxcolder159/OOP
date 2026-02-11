package ru.nsu.ermakov.configs;

import ru.nsu.ermakov.products.Product;

import java.util.List;

public class OrderConfig {
    public int orderSpawnRate;
    public int totalOrders;
    public List<Product> productsList;

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

