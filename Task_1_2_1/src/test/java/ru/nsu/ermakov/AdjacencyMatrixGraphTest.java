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
 * Набор тестов для реализации графа через матрицу смежности.
 * Проверяются операции с вершинами, рёбрами, чтением из файла и
 * корректность топологической сортировки.
 */
public class AdjacencyMatrixGraphTest {

    private Graph graph;

    /**
     * Создаёт новый граф на матрице смежности перед каждым тестом и
     * инициализирует вершины 1, 2, 3 и рёбра 1->2 и 1->3.
     */
    @BeforeEach
    public void setUp() {
        graph = new AdjacencyMatrixGraph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
    }

    /**
     * Проверяет, что повторное добавление той же вершины возвращает false,
     * а первая вставка фиксируется успешно.
     */
    @Test
    public void testAddVertexTwice() {
        assertTrue(graph.hasVertex(1));
        assertFalse(graph.addVertex(1));
    }

    /**
     * Проверяет корректное удаление вершины: сама вершина пропадает,
     * и все инцидентные ей рёбра тоже удаляются. Остальные рёбра
     * графа должны остаться неизменными.
     */
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

    /**
     * Проверяет добавление и удаление рёбер.
     * В частности, что:
     * - новое ребро можно добавить;
     * - существующее ребро можно удалить;
     * - повторное удаление отдаёт false.
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
     * Проверяет метод получения соседей вершины.
     * Ожидается, что у вершины 1 соседи {2, 3}.
     * Также проверяется, что запрос соседей у отсутствующей вершины
     * приводит к IllegalArgumentException.
     */
    @Test
    public void testGetNeighbors() {
        Set<Integer> neigh = graph.getNeighbors(1);
        assertEquals(Set.of(2, 3), neigh);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.getNeighbors(999)
        );
    }

    /**
     * Проверяет корректность подсчёта количества вершин и рёбер,
     * а также то, что эти счётчики обновляются после добавления ребра
     * и удаления вершины.
     */
    @Test
    public void testCountsAfterOps() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(2, 3);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(3);
        assertEquals(2, graph.getVertexCount());
    }

    /**
     * Проверяет очистку графа методом clear():
     * - все вершины исчезают,
     * - все рёбра исчезают,
     * - множества вершин становятся пустыми.
     */
    @Test
    public void testClear() {
        graph.clear();
        assertEquals(0, graph.getVertexCount());
        assertEquals(0, graph.getEdgeCount());
        assertTrue(graph.getVertices().isEmpty());
    }

    /**
     * Проверяет загрузку графа из файла фиксированного формата.
     */
    @Test
    public void testLoadFromFile() throws IOException {
        Path tmp = Files.createTempFile("graph", ".txt");
        Files.writeString(tmp, "4 3\n" + "1 2 3 4\n" + "1 2\n" + "1 3\n" + "3 4\n");

        Graph loaded = new AdjacencyMatrixGraph();
        loaded.loadFromFile(tmp);

        assertEquals(Set.of(1, 2, 3, 4), loaded.getVertices());
        assertTrue(loaded.hasEdge(1, 2));
        assertTrue(loaded.hasEdge(1, 3));
        assertTrue(loaded.hasEdge(3, 4));
        assertFalse(loaded.hasEdge(2, 1));

        List<Integer> order = TopologicalSorter.topologicalSort(loaded);
        assertEquals(4, order.size());
    }

    /**
     * Проверяет, что топологическая сортировка падает с исключением
     * на графе, содержащем цикл.
     */
    @Test
    public void testTopologicalSortCycle() {
        graph.addEdge(2, 1);
        assertThrows(
                IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph)
        );
    }
}
