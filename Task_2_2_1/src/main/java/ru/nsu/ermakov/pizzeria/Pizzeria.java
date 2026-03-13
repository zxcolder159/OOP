package ru.nsu.ermakov.pizzeria;

import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.products.Drink;
import ru.nsu.ermakov.products.Food;
import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.staff.Baker;
import ru.nsu.ermakov.staff.Barista;
import ru.nsu.ermakov.staff.Courier;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Pizzeria {
    private final List<Baker> bakers;
    private final List<Courier> couriers;
    private final List<Barista> baristas;
    private final Warehouse warehouse;
    List<Thread> threads = new ArrayList<>();

    /**
     * Конструктор, как трек у Бабангиды.
     */
    public Pizzeria(PizzaConfig pizzaConfig, Warehouse warehouse, CountDownLatch latch) {
        this.warehouse = warehouse;
        this.bakers = new ArrayList<>();
        this.couriers = new ArrayList<>();
        this.baristas = new ArrayList<>();
        for (PizzaConfig.BakerData bakerData : pizzaConfig.bakers) {
            this.bakers.add(new Baker(bakerData.name, warehouse));
        }

        for (PizzaConfig.BaristaData baristaData : pizzaConfig.baristas) {
            this.baristas.add(new Barista(baristaData.name, warehouse));
        }

        for (PizzaConfig.CourierData courierData : pizzaConfig.couriers) {
            this.couriers.add(new Courier(courierData.boxSize, warehouse, latch));
        }
        for (Baker baker : bakers) {
            Thread temp = new Thread(baker);
            threads.add(temp);
            temp.start();
        }
        for (Courier courier : couriers) {
            Thread temp = new Thread(courier);
            threads.add(temp);
            temp.start();
        }
        for (Barista barista : baristas) {
            Thread temp = new Thread(barista);
            threads.add(temp);
            temp.start();
        }
    }

    /**
     * Ищет пекаря с минимальным размером очереди, чтобы добавить ему заказ.
     */
    public void delegateOrder(Product product) {
        int minSize = Integer.MAX_VALUE;

        switch (product) {
            case Food food -> {
                Baker bestBaker = null;
                for (Baker b : bakers) {
                    if (b.getOrderSize() < minSize) {
                        minSize = b.getOrderSize();
                        bestBaker = b;
                    }
                }
                if (bestBaker != null) {
                    bestBaker.addProductToBaker(food);
                } else {
                    System.out.println("В пиццерии нет поваров!");
                }
            }
            case Drink drink -> {
                Barista bestBarista = null;
                for (Barista b : baristas) {
                    if (b.getOrderSize() < minSize) {
                        minSize = b.getOrderSize();
                        bestBarista = b;
                    }
                }
                if (bestBarista != null) {
                    bestBarista.addProductToBarista(drink);
                } else {
                    System.out.println("В пиццерии нет барист!");
                }
            }
            default -> System.out.println("Непонятно, что это за продукт: " + product);
        }
    }

    /**
     * Останавливает работу пиццерии.
     */
    public void stopPizzeria() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

}
