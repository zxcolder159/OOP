package ru.nsu.ermakov.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.products.CocaCola;
import ru.nsu.ermakov.products.Product;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки функциональности склада.
 * Проверяет хранение и извлечение продуктов со склада.
 */
class WarehouseTest {

    private Warehouse warehouse;
    private static final int WAREHOUSE_SIZE = 10;

    /**
     * Инициализация склада перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        warehouse = new Warehouse(WAREHOUSE_SIZE);
    }

    /**
     * Проверка создания склада.
     */
    @Test
    @DisplayName("Создание склада")
    void testWarehouseCreation() {
        assertEquals(WAREHOUSE_SIZE, warehouse.storageSize);
    }

    /**
     * Проверка добавления продукта на склад.
     */
    @Test
    @DisplayName("Добавление продукта на склад")
    void testAddProduct() throws InterruptedException {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        
        warehouse.addProduct(pizza);
        
        var products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertEquals(1, products.size());
        assertEquals(pizza, products.get(0));
    }

    /**
     * Проверка добавления нескольких продуктов.
     */
    @Test
    @DisplayName("Добавление нескольких продуктов")
    void testAddMultipleProducts() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        CocaCola cola = new CocaCola(1);
        
        pizza1.setOrderId(101);
        pizza2.setOrderId(102);
        cola.setOrderId(103);
        
        warehouse.addProduct(pizza1);
        warehouse.addProduct(pizza2);
        warehouse.addProduct(cola);
        
        var products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertEquals(3, products.size());
    }

    /**
     * Проверка извлечения продуктов со склада.
     */
    @Test
    @DisplayName("Извлечение продуктов со склада")
    void testTakeProduct() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        CocaCola cola = new CocaCola(1);
        
        pizza1.setOrderId(201);
        pizza2.setOrderId(202);
        cola.setOrderId(203);
        
        warehouse.addProduct(pizza1);
        warehouse.addProduct(pizza2);
        warehouse.addProduct(cola);
        
        var products = warehouse.takeProduct(5);
        assertEquals(3, products.size());
        assertTrue(products.contains(pizza1));
        assertTrue(products.contains(pizza2));
        assertTrue(products.contains(cola));
    }

    /**
     * Проверка извлечения продуктов с ограничением по размеру.
     */
    @Test
    @DisplayName("Извлечение с ограничением по размеру")
    void testTakeProductWithSizeLimit() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        CocaCola cola = new CocaCola(1);
        
        pizza1.setOrderId(301);
        pizza2.setOrderId(302);
        cola.setOrderId(303);
        
        warehouse.addProduct(pizza1);
        warehouse.addProduct(pizza2);
        warehouse.addProduct(cola);
        
        var products = warehouse.takeProduct(2);
        assertEquals(1, products.size());
        assertEquals(cola, products.get(0));
        
        products = warehouse.takeProduct(4);
        assertEquals(2, products.size());
        assertTrue(products.contains(pizza1));
        assertTrue(products.contains(pizza2));
    }

    /**
     * Проверка извлечения из пустого склада.
     */
    @Test
    @DisplayName("Извлечение из пустого склада")
    void testTakeProductFromEmpty() throws InterruptedException {
        var products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertTrue(products.isEmpty());
    }

    /**
     * Проверка работы при заполненном складе.
     */
    @Test
    @DisplayName("Работа при заполненном складе")
    void testFullWarehouse() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            Pizza pizza = new Pizza(i);
            pizza.setOrderId(400 + i);
            warehouse.addProduct(pizza);
        }
        
        assertEquals(6, warehouse.takeProduct(WAREHOUSE_SIZE).size());
        
        Pizza largePizza = new Pizza(10);
        largePizza.setOrderId(410);
        
        Thread adder = new Thread(() -> {
            try {
                warehouse.addProduct(largePizza);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        adder.start();
        Thread.sleep(200);
        
        assertTrue(adder.isAlive());
        
        var products = warehouse.takeProduct(1);
        assertEquals(0, products.size());
        
        Thread.sleep(200);
        assertFalse(adder.isAlive());
        
        products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertEquals(1, products.size());
        assertEquals(largePizza, products.get(0));
    }

    /**
     * Проверка добавления продукта размером больше вместимости.
     */
    @Test
    @DisplayName("Добавление продукта размером больше вместимости")
    void testAddProductTooLarge() throws InterruptedException {
        Warehouse smallWarehouse = new Warehouse(1);
        
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(500);
        
        Thread adder = new Thread(() -> {
            try {
                smallWarehouse.addProduct(pizza);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        adder.start();
        Thread.sleep(200);
        
        assertTrue(adder.isAlive());
        
        adder.interrupt();
        adder.join(1000);
    }

    /**
     * Проверка извлечения продуктов с нулевым лимитом.
     */
    @Test
    @DisplayName("Извлечение с нулевым лимитом")
    void testTakeProductZeroLimit() throws InterruptedException {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(600);
        
        warehouse.addProduct(pizza);
        
        var products = warehouse.takeProduct(0);
        assertTrue(products.isEmpty());
        
        products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertEquals(1, products.size());
        assertEquals(pizza, products.get(0));
    }

    /**
     * Проверка многопоточного доступа к складу.
     */
    @Test
    @DisplayName("Многопоточный доступ к складу")
    void testConcurrentAccess() throws InterruptedException {
        final int THREAD_COUNT = 5;
        final int PRODUCTS_PER_THREAD = 2;
        Thread[] adders = new Thread[THREAD_COUNT];
        Thread[] takers = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            adders[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < PRODUCTS_PER_THREAD; j++) {
                        Pizza pizza = new Pizza(threadId * 10 + j);
                        pizza.setOrderId(threadId * 100 + j);
                        warehouse.addProduct(pizza);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            takers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < PRODUCTS_PER_THREAD; j++) {
                        var products = warehouse.takeProduct(2);
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            adders[i].start();
            takers[i].start();
        }
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            adders[i].join();
            takers[i].join();
        }
    }

    /**
     * Проверка работы с null продуктами.
     */
    @Test
    @DisplayName("Работа с null продуктами")
    void testNullProductHandling() throws InterruptedException {
        assertDoesNotThrow(() -> {
            warehouse.addProduct(null);
        });
        
        var products = warehouse.takeProduct(WAREHOUSE_SIZE);
        assertEquals(1, products.size());
        assertNull(products.get(0));
    }
}
