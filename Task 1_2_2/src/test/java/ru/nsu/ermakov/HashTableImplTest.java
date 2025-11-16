
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Юнит-тесты для HashTableImpl.
 */
public class HashTableImplTest {

    @Test
    void testPutAndGet() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        assertTrue(table.isEmpty());
        assertEquals(0, table.size());

        assertNull(table.put("one", 1));
        assertNull(table.put("two", 2));
        assertNull(table.put("three", 3));

        assertEquals(3, table.size());
        assertFalse(table.isEmpty());

        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertEquals(3, table.get("three"));
        assertNull(table.get("four"));
    }

    @Test
    void testPutOverrideExistingValue() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        assertNull(table.put("key", 1));
        Integer old = table.put("key", 2);

        assertEquals(1, old);
        assertEquals(2, table.get("key"));
        assertEquals(1, table.size());
    }

    @Test
    void testUpdateExisting() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("a", 10);
        table.put("b", 20);

        Integer old = table.update("a", 100);
        assertEquals(10, old);
        assertEquals(100, table.get("a"));
    }

    @Test
    void testUpdateMissingThrows() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("a", 1);
        assertThrows(NoSuchElementException.class, () -> table.update("b", 2));
    }

    @Test
    void testRemove() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("x", 5);
        table.put("y", 6);

        assertEquals(5, table.remove("x"));
        assertNull(table.remove("x")); // второй раз уже ничего нет

        assertFalse(table.containsKey("x"));
        assertTrue(table.containsKey("y"));
        assertEquals(1, table.size());
    }

    @Test
    void testContainsKey() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("k1", 1);
        table.put("k2", 2);

        assertTrue(table.containsKey("k1"));
        assertTrue(table.containsKey("k2"));
        assertFalse(table.containsKey("k3"));
    }

    @Test
    void testIterationOverAllElements() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);

        int sum = 0;
        int count = 0;
        for (Map.Entry<String, Integer> e : table) {
            sum += e.getValue();
            count++;
        }

        assertEquals(3, count);
        assertEquals(1 + 2 + 3, sum);
    }

    @Test
    void testIteratorRemove() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            if ("b".equals(entry.getKey())) {
                it.remove();
            }
        }

        assertFalse(table.containsKey("b"));
        assertEquals(2, table.size());
    }

    @Test
    void testConcurrentModificationExceptionOnExternalModification() {
        HashTable<String, Integer> table = new HashTableImpl<>();

        table.put("a", 1);
        table.put("b", 2);

        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        assertTrue(it.hasNext());

        // Внешняя модификация таблицы
        table.put("c", 3);

        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void testEqualsAndHashCode() {
        HashTable<String, Integer> t1 = new HashTableImpl<>();
        HashTable<String, Integer> t2 = new HashTableImpl<>();

        t1.put("one", 1);
        t1.put("two", 2);

        t2.put("one", 1);
        t2.put("two", 2);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());

        t2.put("three", 3);
        assertNotEquals(t1, t2);
    }

    @Test
    void testResizeKeepsAllElements() {
        HashTable<Integer, Integer> table = new HashTableImpl<>(4, 0.75f);

        int n = 1000;
        for (int i = 0; i < n; i++) {
            table.put(i, i * 10);
        }

        assertEquals(n, table.size());

        for (int i = 0; i < n; i++) {
            assertTrue(table.containsKey(i));
            assertEquals(i * 10, table.get(i));
        }
    }

    @Test
    void testToStringFormat() {
        HashTable<String, Integer> table = new HashTableImpl<>();
        table.put("x", 1);
        table.put("y", 2);

        String s = table.toString();
        assertTrue(s.startsWith("{"));
        assertTrue(s.endsWith("}"));
        assertTrue(s.contains("x=1"));
        assertTrue(s.contains("y=2"));
    }
}