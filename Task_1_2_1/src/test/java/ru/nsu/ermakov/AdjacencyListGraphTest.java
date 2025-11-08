package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.core.Graph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.impl.AdjacencyListGraph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Набор модульных тестов для реализации графа через список смежности.
 */
public class AdjacencyListGraphTest {

    private Graph graph;

    /**
     * Создаёт новый граф перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
    }

    /**
     * Проверяет операции с вершинами.
     */
    @Test
    public void testAddAndHasVertex() {
        assertTrue(graph.hasVertex(1));
        assertTrue(graph.hasVertex(2));
        assertTrue(graph.hasVertex(3));

        assertFalse(graph.hasVertex(4));
        assertTrue(graph.addVertex(4));
        assertTrue(graph.hasVertex(4));

        assertFalse(graph.addVertex(4));
    }

    /**
     * Проверяет, что удаление вершины.
     */
    @Test
    public void testRemoveVertexAlsoRemovesIncidentEdges() {
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(1, 3));

        assertTrue(graph.removeVertex(2));

        assertFalse(graph.hasVertex(2));
        assertFalse(graph.hasEdge(1, 2));

        assertTrue(graph.hasEdge(1, 3));
    }

    /**
     * Проверяет добавление и удаление рёбер.
     */
    @Test
    public void testAddAndRemoveEdge() {
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(1, 3));

        assertFalse(graph.hasEdge(2, 3));

        graph.addEdge(2, 3);
        assertTrue(graph.hasEdge(2, 3));

        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));

        assertFalse(graph.removeEdge(1, 2));
    }

    /**
     * Проверяет метод getNeighbors().
     */
    @Test
    public void testGetNeighbors() {
        Set<Integer> neighborsOf1 = graph.getNeighbors(1);
        assertEquals(Set.of(2, 3), neighborsOf1);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.getNeighbors(999)
        );
    }

    /**
     * Проверяет корректность счётчиков количества вершин и рёбер.
     */
    @Test
    public void testCounts() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(2, 3);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(3);
        assertEquals(2, graph.getVertexCount());
    }

    /**
     * Проверяет метод clear().
     */
    @Test
    public void testClear() {
        graph.clear();
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertTrue(graph.getVertices().isEmpty());
    }

    /**
     * Проверяет загрузку графа из временного файла фиксированного формата.
     */
    @Test
    public void testLoadFromFile() throws IOException {
        Path tmp = Files.createTempFile("graph", ".txt");
        Files.writeString(tmp, "3 2\n" + "10 20 30\n" + "10 20\n" + "10 30\n");

        Graph loadedGraph = new AdjacencyListGraph();
        loadedGraph.loadFromFile(tmp);

        assertEquals(Set.of(10, 20, 30), loadedGraph.getVertices());
        assertTrue(loadedGraph.hasEdge(10, 20));
        assertTrue(loadedGraph.hasEdge(10, 30));
        assertFalse(loadedGraph.hasEdge(20, 10));
        assertEquals(3, loadedGraph.getVertexCount());
        assertEquals(2, loadedGraph.getEdgeCount());

        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.topologicalSort(loaded);

        assertEquals(10, topoOrder.get(0));
        assertEquals(3, topoOrder.size());
    }

    /**
     * Проверяет, что топологическая сортировка на графе с циклом.
     */
    @Test
    public void testTopologicalSortDetectsCycle() {
        graph.addEdge(2, 1);

        assertThrows(
                IllegalStateException.class,
                () -> {
                    TopologicalSorter sorter = new TopologicalSorter();
                    return sorter.topologicalSort(graph);
                }
        );
    }
}
