package ru.nsu.ermakov.staff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.products.CocaCola;
import ru.nsu.ermakov.warehouse.Warehouse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности бариста.
 * Проверяет работу бариста с напитками и складом.
 */
class BaristaTest {

    @Mock
    private Warehouse mockWarehouse;
    
    private Barista barista;
    private static final String BARISTA_NAME = "Тестовый бариста";

    /**
     * Инициализация тестовых данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        barista = new Barista(BARISTA_NAME, mockWarehouse);
    }

    /**
     * Проверка создания бариста.
     */
    @Test
    @DisplayName("Создание бариста")
    void testBaristaCreation() {
        assertEquals(BARISTA_NAME, barista.getName());
        assertEquals(0, barista.getOrderSize());
        assertNotNull(mockWarehouse);
    }

    /**
     * Проверка получения имени бариста.
     */
    @Test
    @DisplayName("Получение имени бариста")
    void testGetName() {
        assertEquals(BARISTA_NAME, barista.getName());
    }

    /**
     * Проверка установки имени бариста.
     */
    @Test
    @DisplayName("Установка имени бариста")
    void testSetName() {
        String newName = "Новое имя бариста";
        barista.setName(newName);
        assertEquals(newName, barista.getName());
    }

    /**
     * Проверка получения размера очереди заказов.
     */
    @Test
    @DisplayName("Получение размера очереди заказов")
    void testGetOrderSize() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());
        
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        
        barista.addProductToBarista(cola1);
        assertEquals(1, barista.getOrderSize());
        
        barista.addProductToBarista(cola2);
        assertEquals(2, barista.getOrderSize());
    }

    /**
     * Проверка добавления продукта в очередь бариста.
     */
    @Test
    @DisplayName("Добавление продукта в очередь бариста")
    void testAddProductToBarista() throws InterruptedException {
        CocaCola cola = new CocaCola(1);
        assertEquals(0, barista.getOrderSize());
        
        barista.addProductToBarista(cola);
        assertEquals(1, barista.getOrderSize());
    }

    /**
     * Проверка работы бариста в отдельном потоке.
     */
    @Test
    @DisplayName("Работа бариста в потоке")
    void testBaristaRun() throws InterruptedException {
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(300);
        
        barista.addProductToBarista(cola);
        
        Thread baristaThread = new Thread(barista);
        baristaThread.start();
        
        Thread.sleep(500);
        
        verify(mockWarehouse, times(1)).addProduct(cola);
        
        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка обработки нескольких заказов.
     */
    @Test
    @DisplayName("Обработка нескольких заказов")
    void testMultipleOrders() throws InterruptedException {
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        CocaCola cola3 = new CocaCola(3);
        
        cola1.setOrderId(301);
        cola2.setOrderId(302);
        cola3.setOrderId(303);
        
        barista.addProductToBarista(cola1);
        barista.addProductToBarista(cola2);
        barista.addProductToBarista(cola3);
        
        assertEquals(3, barista.getOrderSize());
        
        Thread baristaThread = new Thread(barista);
        baristaThread.start();
        
        Thread.sleep(1000);
        
        verify(mockWarehouse, times(3)).addProduct(any(CocaCola.class));
        
        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка прерывания работы бариста.
     */
    @Test
    @DisplayName("Прерывание работы бариста")
    void testBaristaInterruption() throws InterruptedException {
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(304);
        
        barista.addProductToBarista(cola);
        
        Thread baristaThread = new Thread(barista);
        baristaThread.start();
        
        Thread.sleep(100);
        baristaThread.interrupt();
        baristaThread.join(1000);
        
        assertTrue(baristaThread.isInterrupted() || !baristaThread.isAlive());
    }

    /**
     * Проверка работы бариста с пустой очередью.
     */
    @Test
    @DisplayName("Работа с пустой очередью")
    void testBaristaWithEmptyQueue() throws InterruptedException {
        Thread baristaThread = new Thread(barista);
        baristaThread.start();
        
        Thread.sleep(100);
        assertTrue(baristaThread.isAlive());
        
        baristaThread.interrupt();
        baristaThread.join(1000);
        
        assertEquals(0, barista.getOrderSize());
        verify(mockWarehouse, never()).addProduct(any());
    }

    /**
     * Проверка добавления null продукта.
     */
    @Test
    @DisplayName("Добавление null продукта")
    void testAddNullProduct() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());
        
        barista.addProductToBarista(null);
        assertEquals(1, barista.getOrderSize());
    }

    /**
     * Проверка одновременной работы нескольких бариста.
     */
    @Test
    @DisplayName("Одновременная работа нескольких бариста")
    void testMultipleBaristas() throws InterruptedException {
        Barista barista2 = new Barista("Бариста 2", mockWarehouse);
        
        CocaCola cola1 = new CocaCola(1);
        CocaCola cola2 = new CocaCola(2);
        
        cola1.setOrderId(401);
        cola2.setOrderId(402);
        
        barista.addProductToBarista(cola1);
        barista2.addProductToBarista(cola2);
        
        Thread thread1 = new Thread(barista);
        Thread thread2 = new Thread(barista2);
        
        thread1.start();
        thread2.start();
        
        Thread.sleep(500);
        
        verify(mockWarehouse, atLeast(1)).addProduct(any(CocaCola.class));
        
        thread1.interrupt();
        thread2.interrupt();
        thread1.join(1000);
        thread2.join(1000);
    }

    /**
     * Проверка быстрой обработки напитков (нулевое время).
     */
    @Test
    @DisplayName("Быстрая обработка напитков")
    void testFastProcessing() throws InterruptedException {
        CocaCola cola = new CocaCola(1);
        cola.setOrderId(500);
        
        barista.addProductToBarista(cola);
        
        Thread baristaThread = new Thread(barista);
        baristaThread.start();
        
        Thread.sleep(100);
        
        verify(mockWarehouse, times(1)).addProduct(cola);
        
        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Очистка после тестов.
     */
    @AfterEach
    void tearDown() {
        reset(mockWarehouse);
    }
}
