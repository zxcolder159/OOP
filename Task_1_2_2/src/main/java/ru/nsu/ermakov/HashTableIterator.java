package ru.nsu.ermakov;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Отдельный fail-fast итератор для HashTableImpl.
 */
class HashTableIterator<K, V> implements Iterator<Map.Entry<K, V>> {

    private final HashTableImpl<K, V> table;
    private final HashNode<K, V>[] buckets;

    private int bucketIndex = 0;
    private HashNode<K, V> nextNode;
    private HashNode<K, V> lastReturned;

    private int expectedModCount;

    HashTableIterator(HashTableImpl<K, V> table) {
        this.table = table;
        this.buckets = table.getBuckets(); // пакетный доступ
        this.expectedModCount = table.getModCount();
        advanceToNextNonEmptyBucket();
    }

    private void advanceToNextNonEmptyBucket() {
        while (bucketIndex < buckets.length && (nextNode = buckets[bucketIndex]) == null) {
            bucketIndex++;
        }
    }

    private void checkForComodification() {
        if (expectedModCount != table.getModCount()) {
            throw new ConcurrentModificationException("HashTable modified during iteration");
        }
    }

    @Override
    public boolean hasNext() {
        return nextNode != null;
    }

    @Override
    public Map.Entry<K, V> next() {
        checkForComodification();
        if (nextNode == null) {
            throw new NoSuchElementException();
        }
        lastReturned = nextNode;
        // переходим дальше по цепочке
        nextNode = nextNode.next;
        // если цепочка закончилась — следующий бакет
        if (nextNode == null) {
            bucketIndex++;
            if (bucketIndex < buckets.length) {
                advanceToNextNonEmptyBucket();
            }
        }
        return lastReturned;
    }

    @Override
    public void remove() {
        checkForComodification();
        if (lastReturned == null) {
            throw new IllegalStateException("next() was not called or element already removed");
        }
        // удаляем по ключу через таблицу
        table.remove(lastReturned.key);
        lastReturned = null;
        expectedModCount = table.getModCount();
    }
}