package ru.nsu.ermakov.staff;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.nsu.ermakov.products.Food;
import ru.nsu.ermakov.warehouse.Warehouse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функциональности пекаря.
 * Проверяет создание пекаря, управление заказами и обработку еды.
 */
class BakerTest {
    @Mock
    private Warehouse warehouse;
    @Mock
    private Food food;
    @Mock
    private Food anotherFood;

    private Baker baker;

    /**
     * Инициализация тестовых данных перед каждым тестом.
     * Создает моки склада, еды и экземпляр пекаря.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baker = new Baker("TestBaker", warehouse);

        when(food.getId()).thenReturn(1);
        when(food.getCookingTime()).thenReturn(100L);
        when(food.getOrderId()).thenReturn(1);
        when(food.getSize()).thenReturn(2);

        when(anotherFood.getId()).thenReturn(2);
        when(anotherFood.getCookingTime()).thenReturn(200L);
        when(anotherFood.getOrderId()).thenReturn(2);
        when(anotherFood.getSize()).thenReturn(1);
    }

    /**
     * Проверка конструктора пекаря.
     */
    @Test
    void testConstructor() {
        assertEquals("TestBaker", baker.getName());
        assertEquals(0, baker.getOrderSize());
        assertNotNull(baker.cookingItems);
        assertTrue(baker.cookingItems.isEmpty());
    }

    /**
     * Вспомогательный метод для проверки пустоты очереди готовки.
     */
    private boolean isCookingItemsEmpty(Baker b) {
        try {
            java.lang.reflect.Field field = Baker.class.getDeclaredField("cookingItems");
            field.setAccessible(true);
            ru.nsu.ermakov.atomicqueue.AtomicQueue<Food> queue = 
                (ru.nsu.ermakov.atomicqueue.AtomicQueue<Food>) field.get(b);
            return queue.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверка получения имени пекаря.
     */
    @Test
    void testGetName() {
        assertEquals("TestBaker", baker.getName());
    }

    /**
     * Проверка установки имени пекаря.
     */
    @Test
    void testSetName() {
        baker.setName("NewName");
        assertEquals("NewName", baker.getName());
    }

    /**
     * Проверка получения размера заказа.
     */
    @Test
    void testGetOrderSize() throws InterruptedException {
        assertEquals(0, baker.getOrderSize());

        baker.addProductToBaker(food);
        assertEquals(1, baker.getOrderSize());

        baker.addProductToBaker(anotherFood);
        assertEquals(2, baker.getOrderSize());
    }

    /**
     * Проверка добавления продукта пекарю.
     */
    @Test
    void testAddProductToBaker() throws InterruptedException {
        assertEquals(0, baker.getOrderSize());

        baker.addProductToBaker(food);
        assertEquals(1, baker.getOrderSize());

        baker.addProductToBaker(anotherFood);
        assertEquals(2, baker.getOrderSize());
    }

    /**
     * Проверка обработки прерывания во время работы.
     */
    @Test
    void testRunWithInterruptedException() throws InterruptedException {
        baker.addProductToBaker(food);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(50);
        bakerThread.interrupt();
        bakerThread.join(1000);

        verify(warehouse, never()).addProduct(food);
    }

    /**
     * Проверка обработки прерывания во время готовки.
     */
    @Test
    void testRunWithInterruptedExceptionDuringCooking() throws InterruptedException {
        baker.addProductToBaker(food);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(50);
        bakerThread.interrupt();
        bakerThread.join(1000);

        verify(warehouse, never()).addProduct(food);
    }

    /**
     * Проверка продолжения работы после прерывания.
     */
    @Test
    void testRunContinuesAfterInterruption() throws InterruptedException {
        baker.addProductToBaker(food);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(150);
        verify(warehouse, after(1000)).addProduct(food);

        bakerThread.interrupt();
        bakerThread.join(1000);
    }

    /**
     * Проверка работы пекаря без продуктов.
     */
    @Test
    void testRunWithNoProducts() throws InterruptedException {
        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(100);
        assertTrue(bakerThread.isAlive());

        bakerThread.interrupt();
        bakerThread.join(1000);
    }

    /**
     * Проверка параллельного добавления продуктов.
     */
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
                        Food mockFood = mock(Food.class);
                        doReturn(threadId * 100 + j).when(mockFood).getId();
                        doReturn(10L).when(mockFood).getCookingTime();
                        doReturn(threadId * 100 + j).when(mockFood).getOrderId();
                        doReturn(1).when(mockFood).getSize();
                        baker.addProductToBaker(mockFood);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, java.util.concurrent.TimeUnit.SECONDS);
        assertEquals(NUM_THREADS * PRODUCTS_PER_THREAD, baker.getOrderSize());

        executor.shutdown();
    }

    /**
     * Проверка обработки продуктов в порядке очереди.
     */
    @Test
    void testBakerProcessesProductsInOrder() throws InterruptedException {
        baker.addProductToBaker(food);
        baker.addProductToBaker(anotherFood);

        java.util.concurrent.BlockingQueue<Food> processedOrder = new java.util.concurrent.LinkedBlockingQueue<>();

        doAnswer(invocation -> {
            processedOrder.add(invocation.getArgument(0));
            return null;
        }).when(warehouse).addProduct(any(Food.class));

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(400);
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertEquals(2, processedOrder.size());
        assertEquals(food, processedOrder.poll());
        assertEquals(anotherFood, processedOrder.poll());
    }

    /**
     * Проверка влияния изменения имени на строковое представление.
     */
    @Test
    void testSetNameAffectsToString() {
        String originalName = baker.getName();
        baker.setName("NewBakerName");
        assertEquals("NewBakerName", baker.getName());
        assertNotEquals(originalName, baker.getName());
    }

    /**
     * Проверка размера заказа после обработки.
     */
    @Test
    void testOrderSizeAfterProcessing() throws InterruptedException {
        baker.addProductToBaker(food);
        assertEquals(1, baker.getOrderSize());

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(150);
        assertEquals(0, baker.getOrderSize());

        bakerThread.interrupt();
        bakerThread.join(1000);
    }
}
