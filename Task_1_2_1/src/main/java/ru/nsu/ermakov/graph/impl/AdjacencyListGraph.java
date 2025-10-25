package ru.nsu.ermakov.graph.impl;

import ru.nsu.ermakov.graph.core.AbstractGraph;
import ru.nsu.ermakov.graph.core.GraphFileLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Реализация графа через список смежности.
 *
 * adj[v] = множество всех вершин to,
 * таких что есть ребро (v -> to).
 */
public class AdjacencyListGraph extends AbstractGraph {

    private final Map<Integer, Set<Integer>> adj =
            new LinkedHashMap<>();

    @Override
    public boolean addVertex(int v) {
        if (adj.containsKey(v)) {
            return false;
        }
        adj.put(v, new LinkedHashSet<Integer>());
        return true;
    }

    @Override
    public boolean removeVertex(int v) {
        if (!adj.containsKey(v)) {
            return false;
        }
        // удаляем саму вершину
        adj.remove(v);
        // удаляем рёбра, ведущие в неё
        for (Set<Integer> neighbors : adj.values()) {
            neighbors.remove(v);
        }
        return true;
    }

    @Override
    public boolean hasVertex(int v) {
        return adj.containsKey(v);
    }

    @Override
    public boolean addEdge(int from, int to) {
        if (!hasVertex(from) || !hasVertex(to)) {
            throw new IllegalArgumentException(
                    "Both vertices must exist before adding an edge"
            );
        }
        return adj.get(from).add(to);
    }

    @Override
    public boolean removeEdge(int from, int to) {
        if (!hasVertex(from)) {
            return false;
        }
        return adj.get(from).remove(to);
    }

    @Override
    public boolean hasEdge(int from, int to) {
        if (!hasVertex(from)) {
            return false;
        }
        return adj.get(from).contains(to);
    }

    @Override
    public Set<Integer> getVertices() {
        return Collections.unmodifiableSet(adj.keySet());
    }

    @Override
    public Set<Integer> getNeighbors(int v) {
        if (!hasVertex(v)) {
            throw new IllegalArgumentException(
                    "Vertex " + v + " does not exist"
            );
        }
        return Collections.unmodifiableSet(adj.get(v));
    }

    @Override
    public void clear() {
        adj.clear();
    }

    @Override
    public void loadFromFile(Path path) throws IOException {
        GraphFileLoader.load(this, path);
    }
}
