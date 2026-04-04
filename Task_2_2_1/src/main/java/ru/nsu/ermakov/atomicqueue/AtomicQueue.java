package ru.nsu.ermakov.atomicqueue;

import java.util.ArrayDeque;

public class AtomicQueue<T> {
	private final ArrayDeque<T> deque = new ArrayDeque<>();
	private final int capacity;

	public AtomicQueue() {
		this(Integer.MAX_VALUE);
	}

	public AtomicQueue(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Добавляет элемент в очередь. Блокирует если полная.
	 */
	public void add(T item) throws InterruptedException {
		synchronized (this) {
			while (deque.size() >= capacity) {
				wait();
			}
			deque.addLast(item);
			notifyAll();
		}
	}

	/**
	 * Убирает элемент из очереди. Блокирует если пустая.
	 */
	public T poll() throws InterruptedException {
		synchronized (this) {
			while (deque.isEmpty()) {
				wait();
			}
			T item = deque.pollFirst();
			notifyAll();
			return item;
		}
	}

	/**
	 * Проверяет, пуста ли очередь.
	 */
	public boolean isEmpty() {
		synchronized (this) {
			return deque.isEmpty();
		}
	}

	/**
	 * Возвращает размер очереди.
	 */
	public int size() {
		synchronized (this) {
			return deque.size();
		}
	}

	/**
	 * Возвращает вместимость очереди.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Смотрит первый элемент.
	 */
	public T peek() {
		synchronized (this) {
			return deque.peekFirst();
		}
	}
}
