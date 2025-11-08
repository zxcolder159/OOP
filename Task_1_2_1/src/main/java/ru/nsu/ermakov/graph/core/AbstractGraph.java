package ru.nsu.ermakov.graph.core;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * Базовый класс с общей логикой для всех реализаций графа.
 */
public abstract class AbstractGraph implements Graph {

    /**
     * Возвращает количество вершин в графе.
     */
    @Override
    public int getVertexCount() {
        return getVertices().size();
    }

    /**
     * Возвращает количество рёбер в графе.
     */
    @Override
    public int getEdgeCount() {
        return edgesAsSet().size();
    }

    /**
     * Строит множество всех рёбер графа как отсортированное множество.
     */
    protected Set<Edge> edgesAsSet() {
        Set<Edge> result = new TreeSet<>();
        for (Integer from : getVertices()) {
            for (Integer to : getNeighbors(from)) {
                result.add(new Edge(from, to));
            }
        }
        return result;
    }

    /**
     * Сравнивает графы по множеству вершин и множеству рёбер.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractGraph)) {
            return false;
        }
        AbstractGraph g = (AbstractGraph) other;

        return getVertices().equals(g.getVertices())
                && edgesAsSet().equals(g.edgesAsSet());
    }

    /**
     * Вычисляет хэш-код графа по отсортированным вершинам и рёбрам.
     */
    @Override
    public int hashCode() {
        TreeSet<Integer> sortedVertices = new TreeSet<>(getVertices());
        TreeSet<Edge> sortedEdges = new TreeSet<>(edgesAsSet());
        return Objects.hash(sortedVertices, sortedEdges);
    }

    /**
     * Возвращает строковое представление графа в детерминированном виде.
     */
    @Override
    public String toString() {
        TreeSet<Integer> sortedVertices = new TreeSet<>(getVertices());
        TreeSet<Edge> sortedEdges = new TreeSet<>(edgesAsSet());

        StringJoiner edgeJoiner = new StringJoiner(", ", "[", "]");
        for (Edge e : sortedEdges) {
            edgeJoiner.add(e.toString());
        }

        StringJoiner vertexJoiner = new StringJoiner(", ", "[", "]");
        for (Integer v : sortedVertices) {
            vertexJoiner.add(String.valueOf(v));
        }

        return "Graph{vertices="
                + vertexJoiner.toString()
                + ", edges="
                + edgeJoiner.toString()
                + "}";
    }

    @Override
    public abstract List<Integer> topologicalSort(TopologicalSortStrategy strategy);
}
