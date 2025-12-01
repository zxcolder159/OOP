package ru.nsu.ermakov.graph.impl;

import ru.nsu.ermakov.graph.core.AbstractGraph;
import ru.nsu.ermakov.graph.core.GraphFileLoader;
import ru.nsu.ermakov.graph.core.TopologicalSortStrategy;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Реализация графа через матрицу смежности.
 * matrix[i][j] == true значит есть ребро vertices[i] -> vertices[j].
 */
public class AdjacencyMatrixGraph extends AbstractGraph {

    private final ArrayList<Integer> vertices = new ArrayList<>();
    private final Map<Integer, Integer> indexOf = new HashMap<>();
    private boolean[][] matrix = new boolean[0][0];

    /**
     * Добавляет вершину в граф.
     */
    @Override
    public boolean addVertex(int v) {
        if (indexOf.containsKey(v)) {
            return false;
        }
        int oldSize = vertices.size();
        int newSize = oldSize + 1;

        vertices.add(v);
        indexOf.put(v, oldSize);

        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int i = 0; i < oldSize; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, oldSize);
        }
        matrix = newMatrix;
        return true;
    }

    /**
     * Удаляет вершину из графа вместе со всеми инцидентными рёбрами.
     */
    @Override
    public boolean removeVertex(int v) {
        Integer idxObj = indexOf.get(v);
        if (idxObj == null) {
            return false;
        }
        int idx = idxObj;
        int oldSize = vertices.size();
        int newSize = oldSize - 1;

        boolean[][] newMatrix = new boolean[newSize][newSize];
        for (int oldRow = 0, newRow = 0; oldRow < oldSize; oldRow++) {
            if (oldRow == idx) {
                continue;
            }
            for (int oldCol = 0, newCol = 0; oldCol < oldSize; oldCol++) {
                if (oldCol == idx) {
                    continue;
                }
                newMatrix[newRow][newCol] = matrix[oldRow][oldCol];
                newCol++;
            }
            newRow++;
        }

        vertices.remove(idx);
        indexOf.clear();
        for (int i = 0; i < vertices.size(); i++) {
            indexOf.put(vertices.get(i), i);
        }

        matrix = newMatrix;
        return true;
    }

    /**
     * Проверяет, существует ли вершина в графе.
     */
    @Override
    public boolean hasVertex(int v) {
        return indexOf.containsKey(v);
    }

    /**
     * Добавляет ориентированное ребро (from -> to).
     */
    @Override
    public boolean addEdge(int from, int to) {
        Integer fromIdxObj = indexOf.get(from);
        Integer toIdxObj = indexOf.get(to);
        if (fromIdxObj == null || toIdxObj == null) {
            throw new IllegalArgumentException(
                    "Both vertices must exist before adding an edge"
            );
        }

        int fromIdx = fromIdxObj;
        int toIdx = toIdxObj;

        if (matrix[fromIdx][toIdx]) {
            return false;
        }
        matrix[fromIdx][toIdx] = true;
        return true;
    }

    /**
     * Удаляет ориентированное ребро (from -> to), если оно существует.
     */
    @Override
    public boolean removeEdge(int from, int to) {
        Integer fromIdxObj = indexOf.get(from);
        Integer toIdxObj = indexOf.get(to);
        if (fromIdxObj == null || toIdxObj == null) {
            return false;
        }

        int fromIdx = fromIdxObj;
        int toIdx = toIdxObj;

        if (!matrix[fromIdx][toIdx]) {
            return false;
        }
        matrix[fromIdx][toIdx] = false;
        return true;
    }

    /**
     * Проверяет существование ориентированного ребра (from -> to).
     */
    @Override
    public boolean hasEdge(int from, int to) {
        Integer fromIdxObj = indexOf.get(from);
        Integer toIdxObj = indexOf.get(to);
        if (fromIdxObj == null || toIdxObj == null) {
            return false;
        }
        int fromIdx = fromIdxObj;
        int toIdx = toIdxObj;
        return matrix[fromIdx][toIdx];
    }

    /**
     * Возвращает множество всех вершин графа.
     */
    @Override
    public Set<Integer> getVertices() {
        return Collections.unmodifiableSet(
                new LinkedHashSet<>(vertices)
        );
    }

    /**
     * Возвращает множество соседей вершины v (куда есть дуги из v).
     */
    @Override
    public Set<Integer> getNeighbors(int v) {
        Integer rowIdxObj = indexOf.get(v);
        if (rowIdxObj == null) {
            throw new IllegalArgumentException(
                    "Vertex " + v + " does not exist"
            );
        }
        int rowIdx = rowIdxObj;
        LinkedHashSet<Integer> neigh = new LinkedHashSet<>();
        for (int col = 0; col < vertices.size(); col++) {
            if (matrix[rowIdx][col]) {
                neigh.add(vertices.get(col));
            }
        }
        return Collections.unmodifiableSet(neigh);
    }

    /**
     * Полностью очищает граф.
     */
    @Override
    public void clear() {
        vertices.clear();
        indexOf.clear();
        matrix = new boolean[0][0];
    }

    /**
     * Загружает граф из файла фиксированного формата.
     */
    @Override
    public void loadFromFile(Path path) throws IOException {
        GraphFileLoader.load(this, path);
    }

    @Override
    public List<Integer> topologicalSort(TopologicalSortStrategy strategy) {
        return strategy.topologicalSort(this);
    }
}
