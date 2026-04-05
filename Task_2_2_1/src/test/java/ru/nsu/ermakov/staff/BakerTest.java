package ru.nsu.ermakov.staff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.products.Pizza;
import ru.nsu.ermakov.warehouse.Warehouse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности пекаря.
 * Проверяет работу пекаря с заказами и складом.
 */
class BakerTest {

    @Mock
    private Warehouse mockWarehouse;
    
    private Baker baker;
    private static final String BAKER_NAME = "Тестовый пекарь";

    /**
     * Инициализация тестовых данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baker = new Baker(BAKER_NAME, mockWarehouse);
    }

    /**
     * Проверка создания пекаря.
     */
    @Test
    @DisplayName("Создание пекаря")
    void testBakerCreation() {
        assertEquals(BAKER_NAME, baker.getName());
        assertEquals(0, baker.getOrderSize());
        assertNotNull(mockWarehouse);
    }

    /**
     * Проверка получения имени пекаря.
     */
    @Test
    @DisplayName("Получение имени пекаря")
    void testGetName() {
        assertEquals(BAKER_NAME, baker.getName());
    }

    /**
     * Проверка установки имени пекаря.
     */
    @Test
    @DisplayName("Установка имени пекаря")
    void testSetName() {
        String newName = "Новое имя";
        baker.setName(newName);
        assertEquals(newName, baker.getName());
    }

    /**
     * Проверка получения размера очереди заказов.
     */
    @Test
    @DisplayName("Получение размера очереди заказов")
    void testGetOrderSize() throws InterruptedException {
        assertEquals(0, baker.getOrderSize());
        
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        
        baker.addProductToBaker(pizza1);
        assertEquals(1, baker.getOrderSize());
        
        baker.addProductToBaker(pizza2);
        assertEquals(2, baker.getOrderSize());
    }

    /**
     * Проверка добавления продукта в очередь пекаря.
     */
    @Test
    @DisplayName("Добавление продукта в очередь пекаря")
    void testAddProductToBaker() throws InterruptedException {
        Pizza pizza = new Pizza(1);
        assertEquals(0, baker.getOrderSize());
        
        baker.addProductToBaker(pizza);
        assertEquals(1, baker.getOrderSize());
    }

    /**
     * Проверка работы пекаря в отдельном потоке.
     */
    @Test
    @DisplayName("Работа пекаря в потоке")
    void testBakerRun() throws InterruptedException {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(100);
        
        baker.addProductToBaker(pizza);
        
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        
        Thread.sleep(2500);
        
        verify(mockWarehouse, times(1)).addProduct(pizza);
        
        bakerThread.interrupt();
        bakerThread.join(1000);
    }

    /**
     * Проверка обработки нескольких заказов.
     */
    @Test
    @DisplayName("Обработка нескольких заказов")
    void testMultipleOrders() throws InterruptedException {
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        Pizza pizza3 = new Pizza(3);
        
        pizza1.setOrderId(101);
        pizza2.setOrderId(102);
        pizza3.setOrderId(103);
        
        baker.addProductToBaker(pizza1);
        baker.addProductToBaker(pizza2);
        baker.addProductToBaker(pizza3);
        
        assertEquals(3, baker.getOrderSize());
        
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        
        Thread.sleep(7000);
        
        verify(mockWarehouse, times(3)).addProduct(any(Pizza.class));
        
        bakerThread.interrupt();
        bakerThread.join(1000);
    }

    /**
     * Проверка прерывания работы пекаря.
     */
    @Test
    @DisplayName("Прерывание работы пекаря")
    void testBakerInterruption() throws InterruptedException {
        Pizza pizza = new Pizza(1);
        pizza.setOrderId(104);
        
        baker.addProductToBaker(pizza);
        
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        
        Thread.sleep(500);
        bakerThread.interrupt();
        bakerThread.join(1000);
        
        assertTrue(bakerThread.isInterrupted() || !bakerThread.isAlive());
    }

    /**
     * Проверка работы пекаря с пустой очередью.
     */
    @Test
    @DisplayName("Работа с пустой очередью")
    void testBakerWithEmptyQueue() throws InterruptedException {
        Thread bakerThread = new Thread(baker);
        bakerThread.start();
        
        Thread.sleep(100);
        assertTrue(bakerThread.isAlive());
        
        bakerThread.interrupt();
        bakerThread.join(1000);
        
        assertEquals(0, baker.getOrderSize());
        verify(mockWarehouse, never()).addProduct(any());
    }

    /**
     * Проверка добавления null продукта.
     */
    @Test
    @DisplayName("Добавление null продукта")
    void testAddNullProduct() throws InterruptedException {
        assertEquals(0, baker.getOrderSize());
        
        baker.addProductToBaker(null);
        assertEquals(1, baker.getOrderSize());
    }

    /**
     * Проверка одновременной работы нескольких пекарей.
     */
    @Test
    @DisplayName("Одновременная работа нескольких пекарей")
    void testMultipleBakers() throws InterruptedException {
        Baker baker2 = new Baker("Пекарь 2", mockWarehouse);
        
        Pizza pizza1 = new Pizza(1);
        Pizza pizza2 = new Pizza(2);
        
        pizza1.setOrderId(201);
        pizza2.setOrderId(202);
        
        baker.addProductToBaker(pizza1);
        baker2.addProductToBaker(pizza2);
        
        Thread thread1 = new Thread(baker);
        Thread thread2 = new Thread(baker2);
        
        thread1.start();
        thread2.start();
        
        Thread.sleep(3000);
        
        verify(mockWarehouse, atLeast(1)).addProduct(any(Pizza.class));
        
        thread1.interrupt();
        thread2.interrupt();
        thread1.join(1000);
        thread2.join(1000);
    }

    /**
     * Очистка после тестов.
     */
    @AfterEach
    void tearDown() {
        reset(mockWarehouse);
    }
}
