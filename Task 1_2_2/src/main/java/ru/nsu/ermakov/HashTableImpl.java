package ru.nsu.ermakov;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Конкретная реализация HashTable<K,V>.
 */
public class HashTableImpl<K, V> implements HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private HashNode<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;
    private int modCount;

    @SuppressWarnings("unchecked")
    public HashTableImpl(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be > 0");
        }

        int cap = 1;
        while (cap < initialCapacity) {
            cap <<= 1;
        }

        this.table = (HashNode<K, V>[]) new HashNode[cap];
        this.loadFactor = loadFactor;
        this.threshold = (int) (cap * loadFactor);
        this.size = 0;
        this.modCount = 0;
    }

    public HashTableImpl() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // ===== Вспомогательные методы =====

    int getModCount() {
        return modCount;
    }

    HashNode<K, V>[] getBuckets() {
        return table;
    }

    private int index(Object key) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        return h & (table.length - 1);
    }

    private HashNode<K, V> getNode(Object key) {
        int idx = index(key);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                return e;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        HashNode<K, V>[] newTable = (HashNode<K, V>[]) new HashNode[newCapacity];

        for (HashNode<K, V> e : table) {
            while (e != null) {
                HashNode<K, V> next = e.next;
                int idx = indexForResize(e.key, newCapacity);
                e.next = newTable[idx];
                newTable[idx] = e;
                e = next;
            }
        }

        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
        modCount++;
    }

    private int indexForResize(Object key, int capacity) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        return h & (capacity - 1);
    }

    // ===== Реализация интерфейса HashTable =====

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 2. Добавление (k,v)
    @Override
    public V put(K key, V value) {
        if (size + 1 > threshold) {
            resize();
        }

        int idx = index(key);
        for (HashNode<K, V> e = table[idx]; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                modCount++;
                return old; // обновили
            }
        }

        HashNode<K, V> newNode = new HashNode<>(key, value, table[idx]);
        table[idx] = newNode;
        size++;
        modCount++;
        return null;
    }

    // 3. Удаление по ключу
    @Override
    public V remove(Object key) {
        int idx = index(key);
        HashNode<K, V> prev = null;
        HashNode<K, V> cur = table[idx];

        while (cur != null) {
            if (Objects.equals(cur.key, key)) {
                if (prev == null) {
                    table[idx] = cur.next;
                } else {
                    prev.next = cur.next;
                }
                size--;
                modCount++;
                return cur.value;
            }
            prev = cur;
            cur = cur.next;
        }
        return null;
    }

    // 4. Поиск значения по ключу
    @Override
    public V get(Object key) {
        HashNode<K, V> e = getNode(key);
        return (e == null) ? null : e.value;
    }

    // 5. Обновление значения по ключу
    @Override
    public V update(K key, V newValue) {
        HashNode<K, V> e = getNode(key);
        if (e == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        V old = e.value;
        e.value = newValue;
        modCount++;
        return old;
    }

    // 6. Проверка наличия ключа
    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    // 7. Итератор (fail-fast)
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    // 8. Сравнение на равенство с другой хеш-таблицей
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTable<?, ?> other)) return false;

        if (this.size() != other.size()) return false;

        // пробегаем по своим элементам и сравниваем с другой таблицей
        for (Map.Entry<K, V> entry : this) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            Object otherValue = ((HashTable<?, ?>) other).get(key);
            if (!Objects.equals(value, otherValue)) {
                return false;
            }
        }
        return true;
    }

    // 9. Вывод в строку
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (Map.Entry<K, V> entry : this) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }
}