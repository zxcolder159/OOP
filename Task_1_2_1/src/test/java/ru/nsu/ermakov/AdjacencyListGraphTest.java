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
 * <p>
 * Проверяются операции с вершинами и рёбрами, очистка, загрузка из файла
 * и корректность топологической сортировки.
 */
public class AdjacencyListGraphTest {

    private Graph graph;

    /**
     * Создаёт новый граф перед каждым тестом.
     * <p>
     * Граф содержит вершины 1, 2, 3 и дуги 1->2 и 1->3.
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
     * Проверяет операции с вершинами:
     * <ul>
     *   <li>существующие вершины корректно определяются через hasVertex(),</li>
     *   <li>новая вершина успешно добавляется,</li>
     *   <li>повторное добавление той же вершины возвращает false.</li>
     * </ul>
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
     * Проверяет, что удаление вершины:
     * <ul>
     *   <li>убирает саму вершину,</li>
     *   <li>удаляет все рёбра, инцидентные этой вершине,</li>
     *   <li>не трогает остальные рёбра графа.</li>
     * </ul>
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
     * <p>
     * Проверяем:
     * <ul>
     *   <li>новое ребро 2->3 можно добавить,</li>
     *   <li>существующее ребро 1->2 можно удалить,</li>
     *   <li>повторное удаление того же ребра возвращает false.</li>
     * </ul>
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
     * Проверяет метод getNeighbors():
     * <ul>
     *   <li>для вершины 1 соседи должны быть {2, 3},</li>
     *   <li>вызов getNeighbors() для несуществующей вершины
     *   приводит к IllegalArgumentException.</li>
     * </ul>
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
     * Проверяет корректность счётчиков количества вершин и рёбер:
     * <ul>
     *   <li>до модификаций,</li>
     *   <li>после добавления нового ребра,</li>
     *   <li>после удаления вершины.</li>
     * </ul>
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
     * Проверяет метод clear():
     * после вызова не остаётся ни вершин, ни рёбер,
     * а множество вершин становится пустым.
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
     * <p>
     * Формат файла:
     * <pre>
     * n m
     * v1 v2 ... vN
     * from1 to1
     * from2 to2
     * ...
     * </pre>
     *
     * После загрузки проверяется:
     * <ul>
     *   <li>корректно ли считаны вершины,</li>
     *   <li>корректно ли считаны рёбра,</li>
     *   <li>топологическая сортировка даёт ожидаемый порядок.</li>
     * </ul>
     *
     * @throws IOException если не удалось создать или записать временный файл
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

        List<Integer> topoOrder = TopologicalSorter.topologicalSort(loadedGraph);

        assertEquals(10, topoOrder.get(0));
        assertEquals(3, topoOrder.size());
    }

    /**
     * Проверяет, что топологическая сортировка на графе с циклом
     * выбрасывает IllegalStateException.
     * <p>
     * Мы создаём цикл вида 1->2->1.
     */
    @Test
    public void testTopologicalSortDetectsCycle() {
        graph.addEdge(2, 1);

        assertThrows(
                IllegalStateException.class,
                () -> TopologicalSorter.topologicalSort(graph)
        );
    }
}
