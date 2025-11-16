package ru.nsu.ermakov;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Конкретная реализация {@link HashTable} с использованием separate chaining
 * (массив бакетов + связные списки для разрешения коллизий).
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class HashTableImpl<K, V> implements HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private HashNode<K, V>[] table;
    private int size;
    private final float loadFactor;
    private int threshold;
    private int modCount;

    /**
     * Создаёт новую хеш-таблицу с заданной начальной ёмкостью и
     * коэффициентом загрузки.
     *
     * @param initialCapacity начальный размер внутреннего массива (будет округлён
     *                        до ближайшей степени двойки, не меньшей initialCapacity)
     * @param loadFactor      коэффициент загрузки, при превышении которого
     *                        будет происходить расширение таблицы
     * @throws IllegalArgumentException если initialCapacity ≤ 0
     *                                  или loadFactor ≤ 0
     */
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
            cap <<= 1; // делаем степень двойки
        }

        this.table = (HashNode<K, V>[]) new HashNode[cap];
        this.loadFactor = loadFactor;
        this.threshold = (int) (cap * loadFactor);
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * Создаёт новую хеш-таблицу с параметрами по умолчанию.
     * Начальная ёмкость — 16, коэффициент загрузки — 0.75.
     */
    public HashTableImpl() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // ===== пакетные методы для итератора =====

    /**
     * Возвращает счётчик модификаций, используемый для fail-fast итератора.
     *
     * @return текущее значение modCount
     */
    int getModCount() {
        return modCount;
    }

    /**
     * Возвращает массив бакетов. Используется итератором.
     *
     * @return массив бакетов
     */
    HashNode<K, V>[] getBuckets() {
        return table;
    }

    // ===== вспомогательные методы =====

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

    // ===== реализация интерфейса HashTable =====

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
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
                modCount++; // считаем это модификацией для fail-fast
                return old;
            }
        }

        HashNode<K, V> newNode = new HashNode<>(key, value, table[idx]);
        table[idx] = newNode;
        size++;
        modCount++;
        return null;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        HashNode<K, V> e = getNode(key);
        return (e == null) ? null : e.value;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    /**
     * Возвращает fail-fast итератор по парам (ключ, значение).
     *
     * @return итератор по элементам таблицы
     */
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    /**
     * Сравнивает эту таблицу с другой на равенство по содержимому.
     *
     * @param o объект для сравнения
     * @return {@code true}, если обе таблицы содержат одинаковые пары (ключ, значение)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashTableImpl<?, ?> other)) return false;

        if (this.size != other.size) {
            return false;
        }

        for (Map.Entry<K, V> entry : this) {
            K key = entry.getKey();
            V value = entry.getValue();

            Object otherValue = other.get(key);
            if (!Objects.equals(value, otherValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Вычисляет хеш-код таблицы как сумму хеш-кодов её элементов.
     *
     * @return хеш-код таблицы
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (Map.Entry<K, V> entry : this) {
            h += Objects.hashCode(entry.getKey()) ^ Objects.hashCode(entry.getValue());
        }
        return h;
    }

    /**
     * Возвращает строковое представление таблицы в формате
     * {@code {key1=value1, key2=value2, ...}}.
     *
     * @return строковое представление таблицы
     */
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (Map.Entry<K, V> entry : this) {
            joiner.add(entry.getKey() + "=" + entry.getValue());
        }
        return joiner.toString();
    }
}
