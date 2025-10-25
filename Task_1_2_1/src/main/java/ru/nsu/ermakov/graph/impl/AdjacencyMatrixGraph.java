package ru.nsu.ermakov.graph.impl;

import ru.nsu.ermakov.graph.core.AbstractGraph;
import ru.nsu.ermakov.graph.core.GraphFileLoader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Реализация графа через матрицу смежности.
 * matrix[i][j] == true  значит есть ребро
 * vertices[i] -> vertices[j].
 */
public class AdjacencyMatrixGraph extends AbstractGraph {

    private final ArrayList<Integer> vertices = new ArrayList<>();
    private final Map<Integer, Integer> indexOf = new HashMap<>();
    private boolean[][] matrix = new boolean[0][0];

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
        for (int iOld = 0, iNew = 0; iOld < oldSize; iOld++) {
            if (iOld == idx) {
                continue;
            }
            for (int jOld = 0, jNew = 0; jOld < oldSize; jOld++) {
                if (jOld == idx) {
                    continue;
                }
                newMatrix[iNew][jNew] = matrix[iOld][jOld];
                jNew++;
            }
            iNew++;
        }

        vertices.remove(idx);
        indexOf.clear();
        for (int i = 0; i < vertices.size(); i++) {
            indexOf.put(vertices.get(i), i);
        }

        matrix = newMatrix;
        return true;
    }

    @Override
    public boolean hasVertex(int v) {
        return indexOf.containsKey(v);
    }

    @Override
    public boolean addEdge(int from, int to) {
        Integer iObj = indexOf.get(from);
        Integer jObj = indexOf.get(to);
        if (iObj == null || jObj == null) {
            throw new IllegalArgumentException(
                    "Both vertices must exist before adding an edge"
            );
        }

        int i = iObj;
        int j = jObj;

        if (matrix[i][j]) {
            return false;
        }
        matrix[i][j] = true;
        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        Integer iObj = indexOf.get(from);
        Integer jObj = indexOf.get(to);
        if (iObj == null || jObj == null) {
            return false;
        }

        int i = iObj;
        int j = jObj;

        if (!matrix[i][j]) {
            return false;
        }
        matrix[i][j] = false;
        return true;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        Integer iObj = indexOf.get(from);
        Integer jObj = indexOf.get(to);
        if (iObj == null || jObj == null) {
            return false;
        }
        int i = iObj;
        int j = jObj;
        return matrix[i][j];
    }

    @Override
    public Set<Integer> getVertices() {
        return Collections.unmodifiableSet(
                new LinkedHashSet<>(vertices)
        );
    }

    @Override
    public Set<Integer> getNeighbors(int v) {
        Integer iObj = indexOf.get(v);
        if (iObj == null) {
            throw new IllegalArgumentException(
                    "Vertex " + v + " does not exist"
            );
        }
        int i = iObj;
        LinkedHashSet<Integer> neigh = new LinkedHashSet<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (matrix[i][j]) {
                neigh.add(vertices.get(j));
            }
        }
        return Collections.unmodifiableSet(neigh);
    }

    @Override
    public void clear() {
        vertices.clear();
        indexOf.clear();
        matrix = new boolean[0][0];
    }

    @Override
    public void loadFromFile(Path path) throws IOException {
        GraphFileLoader.load(this, path);
    }
}
