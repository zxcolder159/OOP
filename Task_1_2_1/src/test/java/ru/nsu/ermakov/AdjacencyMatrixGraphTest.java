package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.core.Graph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.impl.AdjacencyMatrixGraph;

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
 * Тесты для реализации графа через матрицу смежности.
 */
public class AdjacencyMatrixGraphTest {

    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
    }

    @Test
    public void testAddVertexTwice() {
        assertTrue(graph.hasVertex(1));
        assertFalse(graph.addVertex(1));
    }

    @Test
    public void testRemoveVertexRebuildsMatrix() {
        assertTrue(graph.hasVertex(2));
        assertTrue(graph.hasEdge(1, 2));

        assertTrue(graph.removeVertex(2));
        assertFalse(graph.hasVertex(2));
        assertFalse(graph.hasEdge(1, 2));

        // Остальные рёбра должны остаться
        assertTrue(graph.hasVertex(1));
        assertTrue(graph.hasVertex(3));
        assertTrue(graph.hasEdge(1, 3));
    }

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

    @Test
    public void testGetNeighbors() {
        Set<Integer> neigh = graph.getNeighbors(1);
        assertEquals(Set.of(2, 3), neigh);

        assertThrows(IllegalArgumentException.class,
                () -> graph.getNeighbors(999));
    }

    @Test
    public void testCountsAfterOps() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(2, 3);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(3);
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
        Path tmp = Files.createTempFile("graph", ".txt");
        Files.writeString(tmp,
                "4 3\n" +
                        "1 2 3 4\n" +
                        "1 2\n" +
                        "1 3\n" +
                        "3 4\n"
        );

        Graph g = new AdjacencyMatrixGraph();
        g.loadFromFile(tmp);

        assertEquals(Set.of(1, 2, 3, 4), g.getVertices());
        assertTrue(g.hasEdge(1, 2));
        assertTrue(g.hasEdge(1, 3));
        assertTrue(g.hasEdge(3, 4));
        assertFalse(g.hasEdge(2, 1));

        List<Integer> order = TopologicalSorter.topologicalSort(g);
        assertEquals(4, order.size());
    }

    @Test
    public void testTopologicalSortCycle() {
        graph.addEdge(2, 1);
        assertThrows(IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph));
    }
}
