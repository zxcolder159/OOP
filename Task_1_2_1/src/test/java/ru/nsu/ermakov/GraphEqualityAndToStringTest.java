package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.impl.AdjacencyListGraph;
import ru.nsu.ermakov.graph.impl.AdjacencyMatrixGraph;
import ru.nsu.ermakov.graph.impl.IncidenceMatrixGraph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.core.Graph;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Проверка equals/hashCode/toString для разных реализаций графа.
 */
public class GraphEqualityAndToStringTest {

    private Graph createSampleGraph(Graph g) {
        g.addVertex(10);
        g.addVertex(20);
        g.addVertex(30);
        g.addEdge(10, 20);
        g.addEdge(10, 30);
        return g;
    }

    @Test
    public void testDifferentImplementationsAreEqual() {
        Graph gList = createSampleGraph(new AdjacencyListGraph());
        Graph gMatrix = createSampleGraph(new AdjacencyMatrixGraph());
        Graph gInc = createSampleGraph(new IncidenceMatrixGraph());

        // equals должен сработать поперёк реализаций
        assertTrue(gList.equals(gMatrix));
        assertTrue(gMatrix.equals(gInc));
        assertTrue(gList.equals(gInc));

        // hashCode тоже должен совпадать, раз графы эквивалентны
        assertEquals(gList.hashCode(), gMatrix.hashCode());
        assertEquals(gMatrix.hashCode(), gInc.hashCode());
    }

    @Test
    public void testGraphsNotEqualAfterModification() {
        Graph g1 = createSampleGraph(new AdjacencyListGraph());
        Graph g2 = createSampleGraph(new AdjacencyMatrixGraph());

        // модифицируем второй граф
        g2.addVertex(999);

        assertNotEquals(g1, g2);
    }

    @Test
    public void testToStringIsDeterministicAndReadable() {
        Graph g = createSampleGraph(new AdjacencyListGraph());
        String s = g.toString();

        // ожидаем строго отсортированное представление
        // порядок вершин и рёбер детерминирован, т.к. используется TreeSet
        String expected =
                "Graph{vertices=[10, 20, 30], edges=[10->20, 10->30]}";

        assertEquals(expected, s);
    }

    @Test
    public void testTopologicalSorterOnSampleGraph() {
        Graph g = createSampleGraph(new AdjacencyListGraph());
        List<Integer> topo = TopologicalSorter.topologicalSort(g);

        // в корректном топопорядке 10 обязан быть до 20 и 30
        int idx10 = topo.indexOf(10);
        int idx20 = topo.indexOf(20);
        int idx30 = topo.indexOf(30);

        assertTrue(idx10 < idx20);
        assertTrue(idx10 < idx30);
    }
}
