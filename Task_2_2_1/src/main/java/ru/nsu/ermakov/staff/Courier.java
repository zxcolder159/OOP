package ru.nsu.ermakov.staff;

import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Курьер из пиццерии.
 */
public class Courier implements Runnable{
    private int boxSize;

    private final Warehouse warehouse;
    private List<Product> productsOnCourier;
    private volatile int deliveryTime;
    /**
     * Конструктор.
     */
    public Courier (int boxSize, int deliveryTime, Warehouse warehouse) {
        setBoxSize(boxSize);
        productsOnCourier = new ArrayList<>();
        this.deliveryTime = deliveryTime;
        this.warehouse = warehouse;
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
     * Сеттер времени доставки курьера в зависимости от заказа.
     */

    public void setDeliveryTime (int time) {
        deliveryTime = time;
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
                Thread.sleep(deliveryTime);
                productsOnCourier.clear();
                System.out.println("Курьер доставил заказы и возвращается");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
