package ru.nsu.ermakov.warehouse;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.products.Product;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки склада.
 * Проверяет базовую функциональность склада: добавление и изъятие продуктов.
 */
class WarehouseTest {

    private Warehouse warehouse;
    private Pizza testPizza;

    /**
     * Инициализация тестовых данных перед каждым тестом.
     * Создает склад и тестовую пиццу.
     */
    @BeforeEach
    void setUp() {
        warehouse = new Warehouse(10);
        testPizza = new Pizza(1);
    }

    /**
     * Проверка создания склада.
     */
    @Test
    @DisplayName("Создание склада")
    void testWarehouseCreation() {
        assertEquals(10, warehouse.storageSize);
        assertNotNull(warehouse.storage);
    }

    /**
     * Проверка добавления продукта в пустой склад.
     */
    @Test
    @DisplayName("Добавление продукта в пустой склад")
    void testAddProductToEmptyWarehouse() throws InterruptedException {
        warehouse.addProduct(testPizza);
        
        ArrayList<Product> products = warehouse.takeProduct(10);
        assertEquals(1, products.size());
        assertEquals(testPizza.getId(), products.get(0).getId());
    }

    /**
     * Проверка добавления нескольких продуктов.
     */
    @Test
    @DisplayName("Добавление нескольких продуктов")
    void testAddMultipleProducts() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        Pizza pizza3 = new Pizza(3);
        
        warehouse.addProduct(pizza1);
        warehouse.addProduct(pizza2);
        warehouse.addProduct(pizza3);
        
        ArrayList<Product> products = warehouse.takeProduct(10);
        assertEquals(3, products.size());
    }

    /**
     * Проверка изъятия продуктов с ограничением по размеру.
     */
    @Test
    @DisplayName("Изъятие продуктов с ограничением по размеру")
    void testTakeProductWithSizeLimit() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        Pizza pizza3 = new Pizza(3);
        
        warehouse.addProduct(pizza1);
        warehouse.addProduct(pizza2);
        warehouse.addProduct(pizza3);
        
        ArrayList<Product> products = warehouse.takeProduct(4);
        assertEquals(2, products.size());
    }

    /**
     * Проверка добавления продукта в полный склад.
     */
    @Test
    @DisplayName("Добавление продукта в полный склад")
    void testAddProductToFullWarehouse() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            warehouse.addProduct(new Pizza(i));
        }
        
        Thread addThread = new Thread(() -> {
            try {
                warehouse.addProduct(new Pizza(10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        addThread.start();
        Thread.sleep(50);
        assertTrue(addThread.isAlive());
        
        warehouse.takeProduct(2);
        addThread.join(1000);
        assertFalse(addThread.isAlive());
    }

    /**
     * Проверка что продукт остается на складе если он не помещается в контейнер.
     */
    @Test
    @DisplayName("Продукт остается на складе если не помещается в контейнер")
    void testProductStaysInWarehouseWhenTooLarge() throws InterruptedException {
        warehouse.addProduct(testPizza);
        
        warehouse.takeProduct(1);
        
        ArrayList<Product> products = warehouse.takeProduct(2);
        assertEquals(1, products.size());
    }

    /**
     * Проверка работы с нулевым размером склада.
     */
    @Test
    @DisplayName("Работа с нулевым размером склада")
    void testZeroSizeWarehouse() {
        Warehouse zeroWarehouse = new Warehouse(0);
        assertEquals(0, zeroWarehouse.storageSize);
    }

    /**
     * Проверка последовательного добавления и изъятия продуктов.
     */
    @Test
    @DisplayName("Последовательное добавление и изъятие продуктов")
    void testSequentialAddAndTake() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            warehouse.addProduct(new Pizza(i));
            ArrayList<Product> products = warehouse.takeProduct(2);
            assertEquals(1, products.size());
        }
    }
}
