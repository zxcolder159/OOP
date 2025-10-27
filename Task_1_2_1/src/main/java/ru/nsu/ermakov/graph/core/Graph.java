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
     *
     * @param v id вершины
     * @return true, если вершина существовала и была удалена
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
     * @return количество вершин.
     */
    int getVertexCount();

    /**
     * @return количество рёбер.
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
}
