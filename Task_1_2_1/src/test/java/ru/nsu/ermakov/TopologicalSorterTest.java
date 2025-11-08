package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.impl.AdjacencyListGraph;
import ru.nsu.ermakov.graph.impl.AdjacencyMatrixGraph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.core.Graph;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для TopologicalSorter (алгоритм Канна).
 * Проверяется корректность топологического порядка и обработка циклов.
 */
public class TopologicalSorterTest {

    /**
     * Проверяет, что топологическая сортировка на ацикличном графе.
     */
    @Test
    public void testTopologicalOrderIsValid() {
        Graph g = new AdjacencyListGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);

        g.addEdge(1, 2);
        g.addEdge(1, 3);

        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.topologicalSort(g);

        final int idx1 = order.indexOf(1);
        final int idx2 = order.indexOf(2);
        final int idx3 = order.indexOf(3);

        // размер должен быть ровно 3
        assertEquals(3, order.size());

        // за пределами диапазона должен быть IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> order.get(3));

        // гарантии относительного порядка: 1 идёт до 2 и до 3
        assertTrue(idx1 < idx2);
        assertTrue(idx1 < idx3);
    }

    /**
     * Проверяет, что при наличии цикла (10 -> 20 -> 10).
     */
    @Test
    public void testCycleThrowsException() {
        Graph g = new AdjacencyMatrixGraph();
        g.addVertex(10);
        g.addVertex(20);

        g.addEdge(10, 20);
        g.addEdge(20, 10); // цикл

        assertThrows(
                IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(g)
        );
    }
}
