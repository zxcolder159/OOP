package ru.nsu.ermakov.pizzeria;

import ru.nsu.ermakov.configs.ConfigReader;
import ru.nsu.ermakov.configs.OrderConfig;
import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.configs.WarehouseConfig;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.products.Product;
import ru.nsu.ermakov.warehouse.Warehouse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class PizzeriaApp {
    public static void main (String[] args) {
        ConfigReader reader = new ConfigReader();
        try {
            OrderConfig orderConfig = reader.readOrderConfig("src/main/resources/OrderConfig.json");
            PizzaConfig pizzaConfig = reader.readPizzaConfig("src/main/resources/StuffBase.json");
            WarehouseConfig warehouseConfig = reader.readWarehouseConfig("src/main/resources/WarehouseConfig.json");
            CountDownLatch latch = new CountDownLatch(orderConfig.totalOrders);
            Warehouse warehouse = new Warehouse(warehouseConfig.storageSize);
            Pizzeria pizzeria = new Pizzeria(pizzaConfig, warehouse, latch);
            for (int i = 1; i <= orderConfig.totalOrders; i++) {
                Product template = orderConfig.productsList.get(ThreadLocalRandom.current()
                        .nextInt(orderConfig.productsList.size()));
                Product newOrder = template.clone();
                newOrder.setOrderId(i);
                pizzeria.delegateOrder(newOrder);
                Thread.sleep(orderConfig.orderSpawnRate);
            }
            latch.await();
            pizzeria.stopPizzeria();
            System.out.println("Пиццерия закрыта.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
