package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.LinkedList;

/**
 * Пекарь из пицеррии.
 */
public class Baker implements Runnable {

    private int cookingSpeed;
    private final LinkedList<Product> cookingItems;
    private Warehouse warehouse;
    /**
     * Конструктор.
     */
    public Baker (int cookingSpeed, Warehouse warehouse) {
        setCookingSpeed(cookingSpeed);
        cookingItems = new LinkedList<>();
        this.warehouse = warehouse;
    }

    /**
     * Геттер скорости.
     */
    public int getCookingSpeed() {
        return cookingSpeed;
    }

    /**
     * Сеттер скорости.
     */
    public void setCookingSpeed(int cookingSpeed) {
        if(cookingSpeed <= 0) {
            throw new IllegalArgumentException("Скорость готовки должна быть больше нуля");
        }
        this.cookingSpeed = cookingSpeed;
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
    public void addProductToBaker(Product product) {
        synchronized (cookingItems) {
            cookingItems.add(product);
            cookingItems.notifyAll();
        }
    }


    /**
     * Логика готовки продукта.
     */
    @Override
    public void run () {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Product pizza = null;

                synchronized (cookingItems) {
                    // Если работы нет — повар спит и не тратит ресурсы CPU
                    while (cookingItems.isEmpty()) {
                        cookingItems.wait();
                    }
                    pizza = cookingItems.remove(0);
                }


                Thread.sleep(cookingSpeed);
                warehouse.addProduct(pizza);
                System.out.println("Пекарь приготовил пиццу ID: " + pizza.getId());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
