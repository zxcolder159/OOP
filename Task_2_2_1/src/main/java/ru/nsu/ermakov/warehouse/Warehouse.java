package ru.nsu.ermakov.warehouse;

import ru.nsu.ermakov.atomicqueue.AtomicQueue;
import ru.nsu.ermakov.products.Product;
import java.util.ArrayList;

/**
 * Класс склада для пиццы.
 */
public class Warehouse {
    public final int storageSize;
    private final AtomicQueue<Product> storage;
    private int countOfProducts;

    /**
     * Конструктор.
     */
    public Warehouse(int storageSize) {
        this.storageSize = storageSize;
        storage = new AtomicQueue<>(storageSize);
        countOfProducts = 0;
    }

    /**
     * Добавить продукт в хранилище.
     */
    public void addProduct(Product product) throws InterruptedException {
        while (countOfProducts + product.getSize() > storageSize) {
            Thread.sleep(100); // Простой等待 пока освободится место
        }
        storage.add(product);
        countOfProducts += product.getSize();
    }

    /**
     * Забрать максимальное количество продуктов с помощью курьера.
     */
    public ArrayList<Product> takeProduct(int maxSize) throws InterruptedException {
        ArrayList<Product> toDeliver = new ArrayList<>();
        int currentBagSize = 0;

        // Берем первый продукт
        Product first = storage.poll();
        if (first != null && first.getSize() <= maxSize) {
            toDeliver.add(first);
            countOfProducts -= first.getSize();
            currentBagSize += first.getSize();
        } else if (first != null) {
            // Возвращаем обратно если не помещается
            storage.add(first);
        }

        // Берем остальные продукты
        while (currentBagSize > 0) {
            Product next = storage.peek();
            if (next != null && currentBagSize + next.getSize() <= maxSize) {
                Product p = storage.poll();
                if (p != null) {
                    toDeliver.add(p);
                    countOfProducts -= p.getSize();
                    currentBagSize += p.getSize();
                }
            } else {
                break;
            }
        }

        return toDeliver;
    }

}
