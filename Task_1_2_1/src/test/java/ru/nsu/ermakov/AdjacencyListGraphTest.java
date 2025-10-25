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
 * Тесты для реализации графа через список смежности.
 */
public class AdjacencyListGraphTest {

    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new AdjacencyListGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
    }

    @Test
    public void testAddAndHasVertex() {
        assertTrue(graph.hasVertex(1));
        assertTrue(graph.hasVertex(2));
        assertTrue(graph.hasVertex(3));

        assertFalse(graph.hasVertex(4));
        assertTrue(graph.addVertex(4));
        assertTrue(graph.hasVertex(4));

        // повторно добавить нельзя
        assertFalse(graph.addVertex(4));
    }

    @Test
    public void testRemoveVertexAlsoRemovesIncidentEdges() {
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(1, 3));

        assertTrue(graph.removeVertex(2));

        // вершины 2 больше нет
        assertFalse(graph.hasVertex(2));
        // ребро 1->2 должно исчезнуть
        assertFalse(graph.hasEdge(1, 2));

        // ребро 1->3 по-прежнему живо
        assertTrue(graph.hasEdge(1, 3));
    }

    @Test
    public void testAddAndRemoveEdge() {
        assertTrue(graph.hasEdge(1, 2));
        assertTrue(graph.hasEdge(1, 3));

        // несуществующее ребро
        assertFalse(graph.hasEdge(2, 3));

        // добавим новое ребро
        graph.addEdge(2, 3);
        assertTrue(graph.hasEdge(2, 3));

        // удалим ребро
        assertTrue(graph.removeEdge(1, 2));
        assertFalse(graph.hasEdge(1, 2));

        // повторное удаление того же ребра вернёт false
        assertFalse(graph.removeEdge(1, 2));
    }

    @Test
    public void testGetNeighbors() {
        Set<Integer> n1 = graph.getNeighbors(1);
        assertEquals(Set.of(2, 3), n1);

        assertThrows(IllegalArgumentException.class, () -> graph.getNeighbors(999));
    }

    @Test
    public void testCounts() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(2, 3);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(3);
        // осталось 2 вершины (1 и 2)
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
        // создаём временный файл формата:
        // n m
        // <n вершин>
        // m строк рёбер
        Path tmp = Files.createTempFile("graph", ".txt");
        Files.writeString(tmp,
                "3 2\n" +
                        "10 20 30\n" +
                        "10 20\n" +
                        "10 30\n"
        );

        Graph loaded = new AdjacencyListGraph();
        loaded.loadFromFile(tmp);

        assertEquals(Set.of(10, 20, 30), loaded.getVertices());
        assertTrue(loaded.hasEdge(10, 20));
        assertTrue(loaded.hasEdge(10, 30));
        assertFalse(loaded.hasEdge(20, 10));
        assertEquals(3, loaded.getVertexCount());
        assertEquals(2, loaded.getEdgeCount());

        List<Integer> topo = TopologicalSorter.topologicalSort(loaded);
        // 10 должен быть первым, так как рёбра идут из 10
        assertEquals(10, topo.get(0));
        assertEquals(3, topo.size());
    }

    @Test
    public void testTopologicalSortDetectsCycle() {
        graph.addEdge(2, 1); // создаём цикл: 1->2->1

        assertThrows(IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph));
    }
}
