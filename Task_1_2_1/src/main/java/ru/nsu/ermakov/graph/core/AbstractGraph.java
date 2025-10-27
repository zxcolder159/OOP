package ru.nsu.ermakov.graph.core;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * Базовый класс с общей логикой для всех реализаций графа.
 */
public abstract class AbstractGraph implements Graph {

    @Override
    public int getVertexCount() {
        return getVertices().size();
    }

    @Override
    public int getEdgeCount() {
        return edgesAsSet().size();
    }

    /**
     * Построить множество всех рёбер (как отсортированное множество).
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

    @Override
    public int hashCode() {
        TreeSet<Integer> sortedVertices = new TreeSet<>(getVertices());
        TreeSet<Edge> sortedEdges = new TreeSet<>(edgesAsSet());
        return Objects.hash(sortedVertices, sortedEdges);
    }

    @Override
    public String toString() {
        TreeSet<Integer> sortedVertices = new TreeSet<>(getVertices());
        TreeSet<Edge> sortedEdges = new TreeSet<>(edgesAsSet());

        StringJoiner vJoiner = new StringJoiner(", ", "[", "]");
        for (Integer v : sortedVertices) {
            vJoiner.add(String.valueOf(v));
        }

        StringJoiner eJoiner = new StringJoiner(", ", "[", "]");
        for (Edge e : sortedEdges) {
            eJoiner.add(e.toString());
        }

        return "Graph{vertices=" + vJoiner.toString()
                + ", edges=" + eJoiner.toString()
                + "}";
    }
}
