package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.LinkedList;

/**
 * Пекарь из пицеррии.
 */
public class Baker implements Runnable {

    private final LinkedList<Product> cookingItems;
    private String name;
    private Warehouse warehouse;
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
    public String getName(){
        return name;
    }

    /**
     * Геттер имени.
     */
    public void setName(String name){
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
                Product product = null;

                synchronized (cookingItems) {
                    // Если работы нет — повар спит и не тратит ресурсы CPU
                    while (cookingItems.isEmpty()) {
                        cookingItems.wait();
                    }
                    product = cookingItems.remove(0);
                }


                Thread.sleep(product.getCookingTime());
                warehouse.addProduct(product);
                System.out.println("Пекарь " + name + " приготовил заказ №" + product.getOrderId()
                + " тип товара по ID" + product.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Пекарь " + name + " закончил смену и уходит домой.");
        }
    }
}
