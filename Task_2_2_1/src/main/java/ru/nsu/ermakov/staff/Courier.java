package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CountDownLatch;
/**
 * Курьер из пиццерии.
 */
public class Courier implements Runnable{
    private int boxSize;
    private final CountDownLatch latch;
    private final Warehouse warehouse;
    private List<Product> productsOnCourier;
    /**
     * Конструктор.
     */
    public Courier (int boxSize, Warehouse warehouse, CountDownLatch latch) {
        setBoxSize(boxSize);
        productsOnCourier = new ArrayList<>();
        this.warehouse = warehouse;
        this.latch = latch;
    }

    /**
     * Геттер размера короба.
     */
    public int getBoxSize() {
        return boxSize;
    }

    /**
     * Сеттер размера короба.
     */
    public void setBoxSize (int boxSize) {
        if(boxSize <= 0) {
            throw new IllegalArgumentException("Размер короба должен вмещать как минимум одну пиццу.");
        }
        this.boxSize = boxSize;
    }


    /**
     * Логика доставки продукта.
     */
    @Override
    public void run () {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                productsOnCourier = warehouse.takeProduct(boxSize);
                System.out.println("Курьез взял заказ, количество" + productsOnCourier.size());
                Thread.sleep(ThreadLocalRandom.current().nextLong(1000, 5001));
                for (int i = 0; i < productsOnCourier.size(); i++) {
                    latch.countDown();
                }
                productsOnCourier.clear();
                System.out.println("Курьер доставил заказы и возвращается");
            }
        } catch (InterruptedException e) {
            System.out.println("Курьер закончил смену и уходит домой.");
        }
    }
}
