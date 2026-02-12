package ru.nsu.ermakov.warehouse;

import ru.nsu.ermakov.products.Product;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс склада для пиццы.
 */
public class Warehouse {
    public final int storageSize;
    private final LinkedList<Product> storage;
    private int countOfProducts;
    /**
     * Конструктор.
     */
    public Warehouse (int storageSize) {
        this.storageSize = storageSize;
        storage = new LinkedList<>();
        countOfProducts = 0;
    }

    /**
     * Добавить продукт в хранилище.
     */
    public synchronized void addProduct(Product product) {
        while (countOfProducts + product.getSize() > storageSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                return;
            }
        }
        storage.add(product);
        countOfProducts += product.getSize();
        notifyAll();
    }

    /**
     * Забрать максимальное количество продуктов с помощью курьера.
     */
    public synchronized ArrayList<Product> takeProduct (int maxSize) {
        while(storage.isEmpty()) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ArrayList<>();
            }
        }
        ArrayList<Product> toDeliver = new ArrayList<>();
        int currentBagSize = 0;

        while (!storage.isEmpty() && (currentBagSize + storage.peek().getSize() <= maxSize)) {
            Product p = storage.remove();
            countOfProducts -= p.getSize();
            currentBagSize += p.getSize();
            toDeliver.add(p);
        }
        notifyAll();
        return toDeliver;
    }

}
