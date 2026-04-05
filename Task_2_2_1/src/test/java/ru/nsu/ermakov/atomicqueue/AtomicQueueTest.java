package ru.nsu.ermakov.atomicqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class AtomicQueueTest {
    private AtomicQueue<String> queue;
    private AtomicQueue<String> boundedQueue;

    @BeforeEach
    void setUp() {
        queue = new AtomicQueue<>();
        boundedQueue = new AtomicQueue<>(3);
    }

    @Test
    void testDefaultConstructor() {
        AtomicQueue<Integer> defaultQueue = new AtomicQueue<>();
        assertEquals(Integer.MAX_VALUE, defaultQueue.getCapacity());
        assertTrue(defaultQueue.isEmpty());
        assertEquals(0, defaultQueue.size());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(3, boundedQueue.getCapacity());
        assertTrue(boundedQueue.isEmpty());
        assertEquals(0, boundedQueue.size());
    }

    @Test
    void testAddAndPoll() throws InterruptedException {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());

        queue.add("test1");
        assertEquals(1, queue.size());
        assertFalse(queue.isEmpty());

        queue.add("test2");
        assertEquals(2, queue.size());

        String item1 = queue.poll();
        assertEquals("test1", item1);
        assertEquals(1, queue.size());

        String item2 = queue.poll();
        assertEquals("test2", item2);
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testPeek() throws InterruptedException {
        assertNull(queue.peek());

        queue.add("test1");
        assertEquals("test1", queue.peek());
        assertEquals(1, queue.size());

        queue.add("test2");
        assertEquals("test1", queue.peek());
        assertEquals(2, queue.size());

        queue.poll();
        assertEquals("test2", queue.peek());
        assertEquals(1, queue.size());
    }

    @Test
    void testBoundedQueueAddBlocksWhenFull() throws InterruptedException {
        boundedQueue.add("item1");
        boundedQueue.add("item2");
        boundedQueue.add("item3");

        assertEquals(3, boundedQueue.size());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger addCompleted = new AtomicInteger(0);

        executor.submit(() -> {
            try {
                boundedQueue.add("item4");
                addCompleted.set(1);
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
        assertEquals(0, addCompleted.get());

        boundedQueue.poll();
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS));
        assertEquals(1, addCompleted.get());
        assertEquals(3, boundedQueue.size());

        executor.shutdown();
    }

    @Test
    void testPollBlocksWhenEmpty() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger pollResult = new AtomicInteger(-1);

        executor.submit(() -> {
            try {
                String result = queue.poll();
                pollResult.set(result.equals("test") ? 1 : 0);
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        assertFalse(latch.await(100, TimeUnit.MILLISECONDS));
        assertEquals(-1, pollResult.get());

        queue.add("test");
        assertTrue(latch.await(100, TimeUnit.MILLISECONDS));
        assertEquals(1, pollResult.get());
        assertTrue(queue.isEmpty());

        executor.shutdown();
    }

    @Test
    void testIsEmpty() throws InterruptedException {
        assertTrue(queue.isEmpty());

        queue.add("test");
        assertFalse(queue.isEmpty());

        queue.poll();
        assertTrue(queue.isEmpty());
    }

    @Test
    void testSize() throws InterruptedException {
        assertEquals(0, queue.size());

        for (int i = 0; i < 5; i++) {
            queue.add("item" + i);
            assertEquals(i + 1, queue.size());
        }

        for (int i = 5; i > 0; i--) {
            queue.poll();
            assertEquals(i - 1, queue.size());
        }
    }

    @Test
    void testGetCapacity() {
        assertEquals(Integer.MAX_VALUE, queue.getCapacity());
        assertEquals(3, boundedQueue.getCapacity());
    }

    @Test
    void testConcurrentOperations() throws InterruptedException {
        final int NUM_THREADS = 10;
        final int OPERATIONS_PER_THREAD = 100;
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        AtomicInteger totalAdded = new AtomicInteger(0);
        AtomicInteger totalRemoved = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        if (j % 2 == 0) {
                            queue.add("thread-" + threadId + "-item-" + j);
                            totalAdded.incrementAndGet();
                        } else {
                            String item = queue.poll();
                            if (item != null) {
                                totalRemoved.incrementAndGet();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        assertEquals(totalAdded.get() - totalRemoved.get(), queue.size());

        executor.shutdown();
    }

    @Test
    @Timeout(5)
    void testInterruptDuringAdd() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                boundedQueue.add("item1");
                boundedQueue.add("item2");
                boundedQueue.add("item3");
                boundedQueue.add("item4");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(100);
        thread.interrupt();
        thread.join(1000);

        assertTrue(thread.isInterrupted());
    }

    @Test
    @Timeout(5)
    void testInterruptDuringPoll() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                queue.poll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(100);
        thread.interrupt();
        thread.join(1000);

        assertTrue(thread.isInterrupted());
    }

    @Test
    void testMultipleNotifyAll() throws InterruptedException {
        final int WAITING_THREADS = 5;
        ExecutorService executor = Executors.newFixedThreadPool(WAITING_THREADS);
        CountDownLatch startLatch = new CountDownLatch(WAITING_THREADS);
        CountDownLatch completeLatch = new CountDownLatch(WAITING_THREADS);

        for (int i = 0; i < WAITING_THREADS; i++) {
            executor.submit(() -> {
                try {
                    startLatch.countDown();
                    String item = queue.poll();
                    assertNotNull(item);
                    completeLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        assertTrue(startLatch.await(1, TimeUnit.SECONDS));
        Thread.sleep(100);

        queue.add("item1");
        queue.add("item2");
        queue.add("item3");
        queue.add("item4");
        queue.add("item5");

        assertTrue(completeLatch.await(1, TimeUnit.SECONDS));
        assertTrue(queue.isEmpty());

        executor.shutdown();
    }
}
