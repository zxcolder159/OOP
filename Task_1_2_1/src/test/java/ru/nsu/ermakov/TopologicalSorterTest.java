package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.impl.AdjacencyListGraph;
import ru.nsu.ermakov.graph.impl.AdjacencyMatrixGraph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.core.Graph;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для TopologicalSorter (алгоритм Канна).
 */
public class TopologicalSorterTest {

    @Test
    public void testTopologicalOrderIsValid() {
        Graph g = new AdjacencyListGraph();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);

        g.addEdge(1, 2);
        g.addEdge(1, 3);

        List<Integer> order = TopologicalSorter.topologicalSort(g);
        // 1 должен идти раньше 2 и 3
        int idx1 = order.indexOf(1);
        int idx2 = order.indexOf(2);
        int idx3 = order.indexOf(3);

        assertEquals(3, order.size());
        // проверяем относительный порядок
        // (не проверяем что именно [1,2,3], допускаем [1,3,2])
        assertThrows(IndexOutOfBoundsException.class, () -> order.get(3));
        // гарантии порядка:
        assertEquals(true, idx1 < idx2);
        assertEquals(true, idx1 < idx3);
    }

    @Test
    public void testCycleThrowsException() {
        Graph g = new AdjacencyMatrixGraph();
        g.addVertex(10);
        g.addVertex(20);

        g.addEdge(10, 20);
        g.addEdge(20, 10); // цикл

        assertThrows(IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(g));
    }
}
