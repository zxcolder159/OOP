package ru.nsu.ermakov.staff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.products.Drink;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaristaTest {
    @Mock
    private Warehouse warehouse;
    @Mock
    private Drink drink;
    @Mock
    private Drink anotherDrink;

    private Barista barista;

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

    @Test
    void testConstructor() {
        assertEquals("TestBarista", barista.getName());
        assertEquals(0, barista.getOrderSize());
        assertNotNull(barista.drinkingItems);
        assertTrue(barista.drinkingItems.isEmpty());
    }

    @Test
    void testGetName() {
        assertEquals("TestBarista", barista.getName());
    }

    @Test
    void testSetName() {
        barista.setName("NewName");
        assertEquals("NewName", barista.getName());
    }

    @Test
    void testGetOrderSize() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());

        barista.addProductToBarista(drink);
        assertEquals(1, barista.getOrderSize());

        barista.addProductToBarista(anotherDrink);
        assertEquals(2, barista.getOrderSize());
    }

    @Test
    void testAddProductToBarista() throws InterruptedException {
        assertEquals(0, barista.getOrderSize());

        barista.addProductToBarista(drink);
        assertEquals(1, barista.getOrderSize());

        barista.addProductToBarista(anotherDrink);
        assertEquals(2, barista.getOrderSize());
    }

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

    @Test
    void testRunWithNoProducts() throws InterruptedException {
        Thread baristaThread = new Thread(barista);
        baristaThread.start();

        Thread.sleep(100);
        assertTrue(baristaThread.isAlive());

        baristaThread.interrupt();
        baristaThread.join(1000);
    }

    @Test
    void testConcurrentAddProducts() throws InterruptedException {
        final int NUM_THREADS = 5;
        final int PRODUCTS_PER_THREAD = 10;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < PRODUCTS_PER_THREAD; j++) {
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

        latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(NUM_THREADS * PRODUCTS_PER_THREAD, barista.getOrderSize());

        executor.shutdown();
    }

    @Test
    void testBaristaProcessesProductsInOrder() throws InterruptedException {
        barista.addProductToBarista(drink);
        barista.addProductToBarista(anotherDrink);

        java.util.concurrent.BlockingQueue<Drink> processedOrder = new java.util.concurrent.LinkedBlockingQueue<>();

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

    @Test
    void testSetNameAffectsToString() {
        String originalName = barista.getName();
        barista.setName("NewBaristaName");
        assertEquals("NewBaristaName", barista.getName());
        assertNotEquals(originalName, barista.getName());
    }

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
