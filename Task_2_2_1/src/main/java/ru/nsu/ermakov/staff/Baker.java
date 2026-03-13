package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.products.Food;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.LinkedList;

/**
 * Пекарь из пицеррии.
 */
public class Baker implements Runnable {

    private final LinkedList<Food> cookingItems;
    private String name;
    private final Warehouse warehouse;

    /**
     * Конструктор.
     */
    public Baker(String name, Warehouse warehouse) {
        this.name = name;
        cookingItems = new LinkedList<>();
        this.warehouse = warehouse;
    }

    /**
     * Геттер имени.
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер имени.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер размера очереди.
     */
    public int getOrderSize() {
        synchronized (cookingItems) {
            return cookingItems.size();
        }

    }

    /**
     * Добавить продукт в список продуктов для готовки.
     */
    public void addProductToBaker(Food food) {
        synchronized (cookingItems) {
            cookingItems.add(food);
            cookingItems.notifyAll();
        }
    }


    /**
     * Логика готовки продукта.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Food food = null;

                synchronized (cookingItems) {
                    while (cookingItems.isEmpty()) {
                        cookingItems.wait();
                    }
                    food = cookingItems.removeFirst();
                }


                Thread.sleep(food.getCookingTime());
                warehouse.addProduct(food);
                System.out.println("Пекарь " + name + " приготовил заказ №" + food.getOrderId()
                    + " тип товара по ID" + food.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Пекарь " + name + " закончил смену и уходит домой.");
        }
    }
}
