package ru.nsu.ermakov;

import java.util.Map;

/**
 * Интерфейс нашей хеш-таблицы.
 */
public interface HashTable<K, V> extends Iterable<Map.Entry<K, V>> {

    int size();

    boolean isEmpty();

    // 2. Добавление пары (k, v)
    // Если ключ уже был — возвращаем старое значение, иначе null.
    V put(K key, V value);

    // 3. Удаление по ключу
    V remove(Object key);

    // 4. Поиск значения по ключу
    V get(Object key);

    // 5. Обновление значения по ключу (если ключа нет — кидаем исключение)
    V update(K key, V newValue);

    // 6. Проверка наличия ключа
    boolean containsKey(Object key);

    // equals и toString оставляем как у Object, реализация переопределит.
}