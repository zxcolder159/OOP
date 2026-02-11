package ru.nsu.ermakov.products;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CocaCola implements Product {
    private final int id;
    private int orderId;
    private final long cookingTime = 0;
    /**
     * Конструктор как трек у Бабангиды.
     */
    public CocaCola(@JsonProperty("id") int id) {
        this.id = id;
    }

    /**
     * Геттер id.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Геттер размера.
     */
    @Override
    public int getSize () {
        return 1;
    }

    /**
     * Геттер времени готовки.
     */
    @Override
    public long getCookingTime() {
        return cookingTime;
    }
    /**
     * Геттер id заказа.
     */
    @Override
    public int getOrderId() {
        return orderId;
    }
    /**
     * Сеттер id заказа.
     */
    @Override
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    /**
     * Клонирование объекта.
     */
    @Override
    public Product clone() {
        return new CocaCola(id);
    }
}
