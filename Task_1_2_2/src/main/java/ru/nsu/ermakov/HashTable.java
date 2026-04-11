package ru.nsu.ermakov;

import java.util.Map;

/**
 * Параметризованный интерфейс хеш-таблицы "ключ-значение".
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public interface HashTable<K, V> extends Iterable<Map.Entry<K, V>> {

    /**
     * Возвращает количество элементов в таблице.
     *
     * @return число пар (ключ, значение)
     */
    int size();

    /**
     * Проверяет, пуста ли таблица.
     *
     * @return {@code true}, если таблица не содержит элементов
     */
    boolean isEmpty();

    /**
     * Добавляет пару ключ-значение.
     * Если ключ уже присутствует, его значение будет заменено.
     *
     * @param key   ключ
     * @param value значение
     * @return предыдущее значение, ассоциированное с этим ключом,
     * или {@code null}, если ключ ранее отсутствовал
     */
    V put(K key, V value);

    /**
     * Удаляет значение по ключу.
     *
     * @param key ключ
     * @return удалённое значение или {@code null}, если такого ключа не было
     */
    V remove(Object key);

    /**
     * Возвращает значение по ключу.
     *
     * @param key ключ
     * @return значение или {@code null}, если ключ отсутствует
     */
    V get(Object key);

    /**
     * Обновляет значение по существующему ключу.
     *
     * @param key      ключ
     * @param newValue новое значение
     * @return предыдущее значение
     * @throws java.util.NoSuchElementException если указанного ключа нет
     */
    V update(K key, V newValue);

    /**
     * Проверяет наличие ключа в таблице.
     *
     * @param key ключ
     * @return {@code true}, если ключ присутствует
     */
    boolean containsKey(Object key);
}
