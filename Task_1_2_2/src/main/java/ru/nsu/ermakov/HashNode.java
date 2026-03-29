package ru.nsu.ermakov;

import java.util.Map;

/**
 * Один элемент цепочки в бакете.
 * Реализует Map.Entry<K,V>, чтобы было удобно использовать в итераторе.
 */
class HashNode<K, V> implements Map.Entry<K, V> {

    final K key;
    V value;
    HashNode<K, V> next;

    HashNode(K key, V value, HashNode<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V newValue) {
        V old = this.value;
        this.value = newValue;
        return old;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}