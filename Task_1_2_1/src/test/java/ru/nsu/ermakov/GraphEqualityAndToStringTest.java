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

    /**
     * Заполняет переданный граф одинаковыми данными:
     */
    private Graph createSampleGraph(Graph graph) {
        graph.addVertex(10);
        graph.addVertex(20);
        graph.addVertex(30);
        graph.addEdge(10, 20);
        graph.addEdge(10, 30);
        return graph;
    }

    /**
     * Проверяет, что три разных реализации графа
     */
    @Test
    public void testDifferentImplementationsAreEqual() {
        Graph incidenceGraph = createSampleGraph(new IncidenceMatrixGraph());
        Graph listGraph = createSampleGraph(new AdjacencyListGraph());
        Graph matrixGraph = createSampleGraph(new AdjacencyMatrixGraph());

        // equals должен сработать поперёк реализаций
        assertTrue(listGraph.equals(matrixGraph));
        assertTrue(matrixGraph.equals(incidenceGraph));
        assertTrue(listGraph.equals(incidenceGraph));

        // hashCode тоже должен совпадать, раз графы эквивалентны
        assertEquals(listGraph.hashCode(), matrixGraph.hashCode());
        assertEquals(matrixGraph.hashCode(), incidenceGraph.hashCode());
    }

    /**
     * Проверяет, что если изменить структуру одного графа
     */
    @Test
    public void testGraphsNotEqualAfterModification() {
        Graph graph1 = createSampleGraph(new AdjacencyListGraph());
        Graph graph2 = createSampleGraph(new AdjacencyMatrixGraph());

        // модифицируем второй граф
        graph2.addVertex(999);

        assertNotEquals(graph1, graph2);
    }

    /**
     * Проверяет, что метод toString() возвращает стабильное,
     */
    @Test
    public void testToStringIsDeterministicAndReadable() {
        Graph graph = createSampleGraph(new AdjacencyListGraph());
        String actualString = graph.toString();

        String expectedString =
                "Graph{vertices=[10, 20, 30], edges=[10->20, 10->30]}";

        assertEquals(expectedString, actualString);
    }

    /**
     * Проверяет, что топологическая сортировка
     */
    @Test
    public void testTopologicalSorterOnSampleGraph() {
        Graph graph = createSampleGraph(new AdjacencyListGraph());
        List<Integer> topo = TopologicalSorter.topologicalSort(graph);

        // в корректном топопорядке 10 обязан быть до 20 и 30
        int idx10 = topo.indexOf(10);
        int idx20 = topo.indexOf(20);
        int idx30 = topo.indexOf(30);

        assertTrue(idx10 < idx20);
        assertTrue(idx10 < idx30);
    }
}
