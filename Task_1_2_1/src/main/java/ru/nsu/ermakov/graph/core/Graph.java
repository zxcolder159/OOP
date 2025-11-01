package ru.nsu.ermakov.graph.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Интерфейс ориентированного графа.
 */
public interface Graph {
    /**
     * Добавляет вершину с данным id.
     */
    boolean addVertex(int v);

    /**
     * Удаляет вершину с данным id (и все инцидентные рёбра).
     */
    boolean removeVertex(int v);

    /**
     * Проверяет наличие вершины.
     */
    boolean hasVertex(int v);

    /**
     * Добавляет ориентированное ребро (from -> to).
     */
    boolean addEdge(int from, int to);

    /**
     * Удаляет ориентированное ребро (from -> to).
     */
    boolean removeEdge(int from, int to);

    /**
     * Проверяет существование ребра (from -> to).
     */
    boolean hasEdge(int from, int to);

    /**
     * Возвращает множество всех вершин графа.
     */
    Set<Integer> getVertices();

    /**
     * Возвращает множество соседей вершины v.
     */
    Set<Integer> getNeighbors(int v);

    /**
     * Возвращает количество вершин.
     */
    int getVertexCount();

    /**
     * Возвращает количество рёбер.
     */
    int getEdgeCount();

    /**
     * Полностью очистить граф (удалить все вершины и рёбра).
     */
    void clear();

    /**
     * Загрузить граф из файла фиксированного формата.
     */
    void loadFromFile(Path path) throws IOException;

    /**
     * Sorts all vertices in ascending order of their identifiers.
     * Implementations that use adjacency representations should
     * reorder internal data structures accordingly.
     */
    void sort();
}
