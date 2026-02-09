package ru.nsu.ermakov.pizzeria;

import ru.nsu.ermakov.configs.ConfigReader;
import ru.nsu.ermakov.configs.OrderConfig;
import ru.nsu.ermakov.configs.PizzaConfig;
import ru.nsu.ermakov.configs.WarehouseConfig;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.warehouse.Warehouse;

public class PizzeriaApp {
    public static void main (String[] args) {
        ConfigReader reader = new ConfigReader();
        try {
            OrderConfig orderConfig = reader.readOrderConfig("src/main/resources/OrderConfig.json");
            PizzaConfig pizzaConfig = reader.readPizzaConfig("src/main/resources/PizzeriasConfig.json");
            WarehouseConfig warehouseConfig = reader.readWarehouseConfig("src/main/resources/WarehouseConfig.json");

            Warehouse warehouse = new Warehouse(warehouseConfig.storageSize);
            Pizzeria pizzeria = new Pizzeria(pizzaConfig, warehouse);
            for (int i = 1; i <= orderConfig.totalOrders; i++) {
                pizzeria.delegateOrder(new Pizza(i));
                Thread.sleep(orderConfig.orderSpawnRate);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}
