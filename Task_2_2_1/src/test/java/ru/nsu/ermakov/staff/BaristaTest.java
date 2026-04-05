package ru.nsu.ermakov.staff;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ru.nsu.ermakov.products.Drink;
import ru.nsu.ermakov.warehouse.Warehouse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для проверки функциональности бариста.
 * Проверяет создание баристы, управление заказами и обработку напитков.
 */
class BaristaTest {
    @Mock
    private Warehouse warehouse;
    @Mock
    private Drink drink;
    @Mock
    private Drink anotherDrink;

    private Barista barista;

    /**
     * Инициализация тестовых данных перед каждым тестом.
     * Создает моки склада, напитков и экземпляр баристы.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        barista = new Barista("TestBarista", warehouse);

        when(drink.getId()).thenReturn(1);
        when(drink.getProcessingTime()).thenReturn(100L);
        when(drink.getOrderId()).thenReturn(1);
        when(drink.getSize()).thenReturn(1);

        when(anotherDrink.getId()).thenReturn(2);
        when(anotherDrink.getProcessingTime()).thenReturn(200L);
        when(anotherDrink.getOrderId()).thenReturn(2);
        when(anotherDrink.getSize()).thenReturn(1);
    }

    /**
     * Проверка конструктора баристы.
     */
    @Test
    void testConstructor() {
        assertEquals("TestBarista", barista.getName());
        assertEquals(0, barista.getOrderSize());
        assertNotNull(barista.drinkingItems);
        assertTrue(barista.drinkingItems.isEmpty());
    }

    /**
     * Проверка получения имени баристы.
     */
    @Test
    void testGetName() {
        assertEquals("TestBarista", barista.getName());
    }

    /**
     * Проверка установки имени баристы.
     */
    @Test
    void testSetName() {
        barista.setName("NewName");
        assertEquals("NewName", barista.getName());
    }

    /**
     * Проверка получения размера заказа.
     */
    @Test
    void testGetOrderSize() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());

        barista.addProductToBarista(drink);
        assertEquals(1, barista.getOrderSize());

        barista.addProductToBarista(anotherDrink);
        assertEquals(2, barista.getOrderSize());
    }

    /**
     * Проверка добавления продукта баристе.
     */
    @Test
    void testAddProductToBarista() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());

        barista.addProductToBarista(drink);
        assertEquals(1, barista.getOrderSize());

        barista.addProductToBarista(anotherDrink);
        assertEquals(2, barista.getOrderSize());
    }

    /**
     * Проверка работы баристы с одним продуктом.
     */
    @Test
    void testRunWithSingleProduct() throws InterruptedException {
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(150);

        verify(warehouse, after(1000)).addProduct(drink);
        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка обработки прерывания во время работы.
     */
    @Test
    void testRunWithInterruptedException() throws InterruptedException {
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(50);
        baristaThread.interrupt();
        baristaThread.join(1000);

        verify(warehouse, never()).addProduct(drink);
    }

    /**
     * Проверка обработки прерывания во время обработки продукта.
     */
    @Test
    void testRunWithInterruptedExceptionDuringProcessing() throws InterruptedException {
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(50);
        baristaThread.interrupt();
        baristaThread.join(1000);

        verify(warehouse, never()).addProduct(drink);
    }

    /**
     * Проверка продолжения работы после прерывания.
     */
    @Test
    void testRunContinuesAfterInterruption() throws InterruptedException {
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(150);
        verify(warehouse, after(1000)).addProduct(drink);

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка работы баристы без продуктов.
     */
    @Test
    void testRunWithNoProducts() throws InterruptedException {
        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(100);
        assertTrue(baristaThread.isAlive());

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка параллельного добавления продуктов.
     */
    @Test
    void testConcurrentAddProducts() throws InterruptedException {
        final int numThreads = 5;
        final int productsPerThread = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < productsPerThread; j++) {
                        Drink mockDrink = mock(Drink.class);
                        doReturn(threadId * 100 + j).when(mockDrink).getId();
                        doReturn(10L).when(mockDrink).getProcessingTime();
                        doReturn(threadId * 100 + j).when(mockDrink).getOrderId();
                        doReturn(1).when(mockDrink).getSize();
                        barista.addProductToBarista(mockDrink);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        assertEquals(numThreads * productsPerThread, barista.getOrderSize());

        executor.shutdown();
    }

    /**
     * Проверка обработки продуктов в порядке очереди.
     */
    @Test
    void testBaristaProcessesProductsInOrder() throws InterruptedException {
        barista.addProductToBarista(drink);
        barista.addProductToBarista(anotherDrink);

        LinkedBlockingQueue<Drink> processedOrder = new LinkedBlockingQueue<>();

        doAnswer(invocation -> {
            processedOrder.add(invocation.getArgument(0));
            return null;
        }).when(warehouse).addProduct(any(Drink.class));

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(400);
        baristaThread.interrupt();
        baristaThread.join(1000);

        assertEquals(2, processedOrder.size());
        assertEquals(drink, processedOrder.poll());
        assertEquals(anotherDrink, processedOrder.poll());
    }

    /**
     * Проверка влияния изменения имени на строковое представление.
     */
    @Test
    void testSetNameAffectsToString() {
        String originalName = barista.getName();
        barista.setName("NewBaristaName");
        assertEquals("NewBaristaName", barista.getName());
        assertNotEquals(originalName, barista.getName());
    }

    /**
     * Проверка размера заказа после обработки.
     */
    @Test
    void testOrderSizeAfterProcessing() throws InterruptedException {
        barista.addProductToBarista(drink);
        assertEquals(1, barista.getOrderSize());

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(150);
        assertEquals(0, barista.getOrderSize());

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка работы с нулевым временем обработки.
     */
    @Test
    void testRunWithZeroProcessingTime() throws InterruptedException {
        doReturn(0L).when(drink).getProcessingTime();
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(50);
        verify(warehouse, after(1000)).addProduct(drink);

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка работы с долгим временем обработки.
     */
    @Test
    void testRunWithLongProcessingTime() throws InterruptedException {
        doReturn(500L).when(drink).getProcessingTime();
        barista.addProductToBarista(drink);

        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(100);
        assertEquals(0, barista.getOrderSize());
        verify(warehouse, never()).addProduct(drink);

        Thread.sleep(500);
        verify(warehouse, after(1000)).addProduct(drink);

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    /**
     * Проверка работы нескольких барист с одним складом.
     */
    @Test
    void testMultipleBaristasWithSameWarehouse() throws InterruptedException {
        Barista barista2 = new Barista("Barista2", warehouse);

        barista.addProductToBarista(drink);
        barista2.addProductToBarista(anotherDrink);

        Thread thread1 = new Thread(barista);
        Thread thread2 = new Thread(barista2);

        thread1.start();
        thread2.start();

        Thread.sleep(300);

        verify(warehouse, after(1000)).addProduct(drink);
        verify(warehouse, after(1000)).addProduct(anotherDrink);

        thread1.interrupt();
        thread2.interrupt();
        thread1.join(1000);
        thread2.join(1000);
    }
}
