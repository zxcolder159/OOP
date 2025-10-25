package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.core.Graph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.impl.IncidenceMatrixGraph;

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
 * Тесты для реализации графа через матрицу инцидентности.
 */
public class IncidenceMatrixGraphTest {

    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new IncidenceMatrixGraph();
        graph.addVertex(10);
        graph.addVertex(20);
        graph.addVertex(30);

        graph.addEdge(10, 20);
        graph.addEdge(10, 30);
    }

    @Test
    public void testHasVertexAndDuplicateAdd() {
        assertTrue(graph.hasVertex(10));
        assertTrue(graph.hasVertex(20));
        assertTrue(graph.hasVertex(30));
        assertFalse(graph.hasVertex(40));

        assertTrue(graph.addVertex(40));
        assertTrue(graph.hasVertex(40));
        assertFalse(graph.addVertex(40));
    }

    @Test
    public void testRemoveVertexCleansEdges() {
        assertTrue(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(10, 30));

        // удаляем вершину 20 -> все рёбра, где она участвовала, уйдут
        assertTrue(graph.removeVertex(20));
        assertFalse(graph.hasVertex(20));

        // ребро 10->20 больше не существует
        assertFalse(graph.hasEdge(10, 20));

        // ребро 10->30 должно остаться
        assertTrue(graph.hasEdge(10, 30));
    }

    @Test
    public void testAddAndRemoveEdge() {
        assertTrue(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(10, 30));

        assertFalse(graph.hasEdge(20, 30));
        graph.addEdge(20, 30);
        assertTrue(graph.hasEdge(20, 30));

        assertTrue(graph.removeEdge(10, 20));
        assertFalse(graph.hasEdge(10, 20));

        // повторно удалить то же ребро нельзя
        assertFalse(graph.removeEdge(10, 20));
    }

    @Test
    public void testGetNeighbors() {
        Set<Integer> n10 = graph.getNeighbors(10);
        // из 10 рёбра идут в 20 и 30
        assertEquals(Set.of(20, 30), n10);

        assertThrows(IllegalArgumentException.class,
                () -> graph.getNeighbors(999));
    }

    @Test
    public void testCounts() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(20, 30);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(30);
        assertEquals(2, graph.getVertexCount());
    }

    @Test
    public void testClear() {
        graph.clear();
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertTrue(graph.getVertices().isEmpty());
    }

    @Test
    public void testLoadFromFile() throws IOException {
        Path tmp = Files.createTempFile("graph-inc", ".txt");
        Files.writeString(tmp,
                "3 2\n" +
                        "100 200 300\n" +
                        "100 200\n" +
                        "100 300\n"
        );

        Graph g = new IncidenceMatrixGraph();
        g.loadFromFile(tmp);

        assertEquals(Set.of(100, 200, 300), g.getVertices());
        assertTrue(g.hasEdge(100, 200));
        assertTrue(g.hasEdge(100, 300));
        assertFalse(g.hasEdge(200, 100));

        List<Integer> topo = TopologicalSorter.topologicalSort(g);
        assertEquals(3, topo.size());
        assertEquals(100, topo.get(0));
    }

    @Test
    public void testTopologicalSortCycle() {
        graph.addEdge(20, 10); // цикл 10->20->10
        assertThrows(IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph));
    }
}
