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
 * Набор модульных тестов для реализации графа через матрицу инцидентности.
 * <p>
 * Проверяются:
 * <ul>
 *   <li>операции с вершинами и рёбрами,</li>
 *   <li>удаление вершин и очистка графа,</li>
 *   <li>загрузка из файла фиксированного формата,</li>
 *   <li>корректность топологической сортировки и обработка цикла.</li>
 * </ul>
 */
public class IncidenceMatrixGraphTest {

    private Graph graph;

    /**
     * Инициализирует новый граф перед каждым тестом.
     * <p>
     * Граф получает вершины 10, 20, 30 и рёбра 10->20 и 10->30.
     */
    @BeforeEach
    public void setUp() {
        graph = new IncidenceMatrixGraph();
        graph.addVertex(10);
        graph.addVertex(20);
        graph.addVertex(30);

        graph.addEdge(10, 20);
        graph.addEdge(10, 30);
    }

    /**
     * Проверяет корректность добавления вершин:
     * <ul>
     *   <li>существующие вершины распознаются методом hasVertex(),</li>
     *   <li>новая вершина (40) успешно добавляется,</li>
     *   <li>повторное добавление той же вершины возвращает false.</li>
     * </ul>
     */
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

    /**
     * Проверяет, что удаление вершины:
     * <ul>
     *   <li>удаляет саму вершину,</li>
     *   <li>удаляет все рёбра, которые вели в неё или выходили из неё,</li>
     *   <li>не затрагивает оставшиеся рёбра.</li>
     * </ul>
     */
    @Test
    public void testRemoveVertexCleansEdges() {
        assertTrue(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(10, 30));

        assertTrue(graph.removeVertex(20));
        assertFalse(graph.hasVertex(20));

        assertFalse(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(10, 30));
    }

    /**
     * Проверяет добавление и удаление рёбер:
     * <ul>
     *   <li>новое ребро 20->30 можно добавить,</li>
     *   <li>существующее ребро 10->20 можно удалить,</li>
     *   <li>повторное удаление того же ребра возвращает false.</li>
     * </ul>
     */
    @Test
    public void testAddAndRemoveEdge() {
        assertTrue(graph.hasEdge(10, 20));
        assertTrue(graph.hasEdge(10, 30));

        assertFalse(graph.hasEdge(20, 30));
        graph.addEdge(20, 30);
        assertTrue(graph.hasEdge(20, 30));

        assertTrue(graph.removeEdge(10, 20));
        assertFalse(graph.hasEdge(10, 20));

        assertFalse(graph.removeEdge(10, 20));
    }

    /**
     * Проверяет корректность метода getNeighbors():
     * <ul>
     *   <li>для вершины 10 соседи должны быть {20, 30},</li>
     *   <li>вызов для несуществующей вершины выбрасывает IllegalArgumentException.</li>
     * </ul>
     */
    @Test
    public void testGetNeighbors() {
        Set<Integer> neighborsOf10 = graph.getNeighbors(10);
        assertEquals(Set.of(20, 30), neighborsOf10);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.getNeighbors(999)
        );
    }

    /**
     * Проверяет корректность счётчиков вершин и рёбер до и после модификаций.
     * <p>
     * После добавления ребра рёбер становится больше.
     * После удаления вершины общее число вершин уменьшается.
     */
    @Test
    public void testCounts() {
        assertEquals(3, graph.getVertexCount());
        assertEquals(2, graph.getEdgeCount());

        graph.addEdge(20, 30);
        assertEquals(3, graph.getEdgeCount());

        graph.removeVertex(30);
        assertEquals(2, graph.getVertexCount());
    }

    /**
     * Проверяет метод clear():
     * после очистки не остаётся вершин, рёбер
     * и множество вершин становится пустым.
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
     * <p>
     * Формат файла:
     * <pre>
     * n m
     * v1 v2 ... vN
     * from1 to1
     * from2 to2
     * ...
     * </pre>
     * После загрузки сравниваются вершины, рёбра,
     * а также корректность топологической сортировки.
     *
     * @throws IOException если не удалось создать или заполнить временный файл
     */
    @Test
    public void testLoadFromFile() throws IOException {
        Path tmp = Files.createTempFile("graph-inc", ".txt");
        Files.writeString(tmp, "3 2\n" + "100 200 300\n" + "100 200\n" + "100 300\n");

        Graph loadedGraph = new IncidenceMatrixGraph();
        loadedGraph.loadFromFile(tmp);

        assertEquals(Set.of(100, 200, 300), loadedGraph.getVertices());
        assertTrue(loadedGraph.hasEdge(100, 200));
        assertTrue(loadedGraph.hasEdge(100, 300));
        assertFalse(loadedGraph.hasEdge(200, 100));

        List<Integer> topoOrder = TopologicalSorter.topologicalSort(loadedGraph);
        assertEquals(3, topoOrder.size());
        assertEquals(100, topoOrder.get(0));
    }

    /**
     * Проверяет, что при наличии цикла граф некорректен
     * для топологической сортировки и алгоритм бросает
     * IllegalStateException.
     */
    @Test
    public void testTopologicalSortCycle() {
        graph.addEdge(20, 10);
        assertThrows(
                IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph)
        );
    }
}
