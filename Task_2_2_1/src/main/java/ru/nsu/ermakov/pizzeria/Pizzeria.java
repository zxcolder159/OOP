package ru.nsu.ermakov.pizzeria;

import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.staff.Baker;
import ru.nsu.ermakov.staff.Courier;
import ru.nsu.ermakov.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class Pizzeria {
    private final List<Baker> bakers;
    private final List<Courier> couriers;
    private Warehouse warehouse;
    List<Thread> threads = new ArrayList<>();

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public Pizzeria (PizzaConfig pizzaConfig, Warehouse warehouse) {
        this.warehouse = warehouse;
        this.bakers = new ArrayList<>();
        this.couriers = new ArrayList<>();
        for(PizzaConfig.BakerData bakerData : pizzaConfig.bakers) {
            this.bakers.add(new Baker(bakerData.cookingSpeed, warehouse));
        }

        for(PizzaConfig.CourierData courierData : pizzaConfig.couriers) {
            this.couriers.add(new Courier(courierData.boxSize, courierData.deliveryTime, warehouse));
        }
        for(Baker baker : bakers) {
            Thread temp = new Thread(baker);
            threads.add(temp);
            temp.start();
        }
        for(Courier courier : couriers) {
            Thread temp = new Thread(courier);
            threads.add(temp);
            temp.start();
        }
    }

    /**
     * Ищет пекаря с минимальным размером очереди, чтобы добавить ему заказ.
     */
    public void delegateOrder(Product product) {
        Baker baker = null;
        int minSize = 2147483647;
        for(Baker b : bakers) {
            if(minSize > b.getOrderSize()) {
                baker = b;
                minSize = baker.getOrderSize();
            }
        }
        if(baker != null) {
            baker.addProductToBaker(product);
        }
        else {
            System.out.println("В пиццерии нет поваров!");
        }
    }

    /**
     * Останавливает работу пиццерии.
     */
    public void stopPizzeria () {
        for(Thread thread : threads) {
            thread.interrupt();
        }
    }

}
