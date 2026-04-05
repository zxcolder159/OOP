package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.atomicqueue.AtomicQueue;
import ru.nsu.ermakov.products.Drink;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.List;

/**
 * Халдей, разносящий напитки.
 */
public class Barista implements Runnable {
    final AtomicQueue<Drink> drinkingItems;
    private String name;
    private final Warehouse warehouse;

    public Barista(String name, Warehouse warehouse) {
        this.name = name;
        this.warehouse = warehouse;
        drinkingItems = new AtomicQueue<>();
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
        return drinkingItems.size();
    }

    /**
     * Добавить продукт в список продуктов для готовки.
     */
    public void addProductToBarista(Drink drink) throws InterruptedException {
        drinkingItems.add(drink);
    }

    /**
     * Логика готовки продукта.
     */
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Drink drink = drinkingItems.poll();


                try {
                    Thread.sleep(drink.getProcessingTime());
                    warehouse.addProduct(drink);
                    System.out.println("Халдей " + name + " приготовил заказ №" + drink.getOrderId()
                            + " тип товара по ID" + drink.getId());
                } catch (InterruptedException e) {
                    System.out.println("Халдей " + name + " закончил смену и уходит домой.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Халдей " + name + " закончил смену и уходит домой.");
        }
    }
}
