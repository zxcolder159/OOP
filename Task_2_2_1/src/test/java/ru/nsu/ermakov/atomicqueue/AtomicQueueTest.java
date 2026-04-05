package ru.nsu.ermakov.atomicqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовый класс для проверки функциональности потокобезопасной очереди AtomicQueue.
 * Проверяет основные операции очереди в многопоточной среде.
 */
class AtomicQueueTest {

    private AtomicQueue<String> queue;
    private AtomicQueue<Integer> boundedQueue;

    /**
     * Инициализация тестовых очередей перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        queue = new AtomicQueue<>();
        boundedQueue = new AtomicQueue<>(3);
    }

    /**
     * Проверка создания очереди без ограничений.
     */
    @Test
    @DisplayName("Создание неограниченной очереди")
    void testUnboundedQueueCreation() {
        AtomicQueue<String> unbounded = new AtomicQueue<>();
        assertEquals(Integer.MAX_VALUE, unbounded.getCapacity());
        assertTrue(unbounded.isEmpty());
        assertEquals(0, unbounded.size());
    }

    /**
     * Проверка создания очереди с ограниченной вместимостью.
     */
    @Test
    @DisplayName("Создание ограниченной очереди")
    void testBoundedQueueCreation() {
        assertEquals(3, boundedQueue.getCapacity());
        assertTrue(boundedQueue.isEmpty());
        assertEquals(0, boundedQueue.size());
    }

    /**
     * Проверка добавления и извлечения элементов.
     */
    @Test
    @DisplayName("Добавление и извлечение элементов")
    void testAddAndPoll() throws InterruptedException {
        queue.add("test1");
        queue.add("test2");
        
        assertEquals(2, queue.size());
        assertFalse(queue.isEmpty());
        
        assertEquals("test1", queue.poll());
        assertEquals("test2", queue.poll());
        
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /**
     * Проверка просмотра первого элемента без удаления.
     */
    @Test
    @DisplayName("Просмотр первого элемента")
    void testPeek() throws InterruptedException {
        queue.add("first");
        queue.add("second");
        
        assertEquals("first", queue.peek());
        assertEquals(2, queue.size());
        assertEquals("first", queue.poll());
        assertEquals("second", queue.peek());
    }

    /**
     * Проверка просмотра пустой очереди.
     */
    @Test
    @DisplayName("Просмотр пустой очереди")
    void testPeekEmpty() {
        assertNull(queue.peek());
        assertTrue(queue.isEmpty());
    }

    /**
     * Проверка ограниченной очереди - блокировка при переполнении.
     */
    @Test
    @DisplayName("Блокировка при переполнении ограниченной очереди")
    void testBoundedQueueBlocking() throws InterruptedException {
        boundedQueue.add(1);
        boundedQueue.add(2);
        boundedQueue.add(3);
        
        assertEquals(3, boundedQueue.size());
        assertEquals(3, boundedQueue.getCapacity());
        
        Thread adder = new Thread(() -> {
            try {
                boundedQueue.add(4);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        adder.start();
        Thread.sleep(100);
        
        assertTrue(adder.isAlive());
        
        boundedQueue.poll();
        Thread.sleep(100);
        
        assertEquals(3, boundedQueue.size());
        adder.join();
    }

    /**
     * Проверка блокировки при извлечении из пустой очереди.
     */
    @Test
    @DisplayName("Блокировка при извлечении из пустой очереди")
    void testEmptyQueueBlocking() throws InterruptedException {
        Thread poller = new Thread(() -> {
            try {
                queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        poller.start();
        Thread.sleep(100);
        
        assertTrue(poller.isAlive());
        
        queue.add("test");
        Thread.sleep(100);
        
        assertTrue(queue.isEmpty());
        poller.join();
    }

    /**
     * Проверка работы с null значениями.
     */
    @Test
    @DisplayName("Работа с null значениями")
    void testNullValues() throws InterruptedException {
        queue.add(null);
        assertEquals(1, queue.size());
        assertNull(queue.poll());
        assertTrue(queue.isEmpty());
    }

    /**
     * Проверка многопоточного доступа.
     */
    @Test
    @DisplayName("Многопоточный доступ")
    void testConcurrentAccess() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final int ITEMS_PER_THREAD = 100;
        Thread[] producers = new Thread[THREAD_COUNT];
        Thread[] consumers = new Thread[THREAD_COUNT];
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            producers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < ITEMS_PER_THREAD; j++) {
                        queue.add("item-" + threadId + "-" + j);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            consumers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < ITEMS_PER_THREAD; j++) {
                        queue.poll();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            producers[i].start();
            consumers[i].start();
        }
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            producers[i].join();
            consumers[i].join();
        }
        
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /**
     * Проверка прерывания потоков при ожидании.
     */
    @Test
    @DisplayName("Прерывание потоков при ожидании")
    void testInterruption() {
        AtomicQueue<String> fullQueue = new AtomicQueue<>(1);
        Thread interruptedThread = new Thread(() -> {
            try {
                fullQueue.add("item1");
                fullQueue.add("item2");
            } catch (InterruptedException e) {
                assertTrue(Thread.currentThread().isInterrupted());
            }
        });
        
        interruptedThread.start();
        try {
            Thread.sleep(100);
            interruptedThread.interrupt();
            interruptedThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
