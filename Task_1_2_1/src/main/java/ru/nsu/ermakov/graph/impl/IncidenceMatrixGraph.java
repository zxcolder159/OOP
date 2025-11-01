package ru.nsu.ermakov.graph.impl;

import ru.nsu.ermakov.graph.core.AbstractGraph;
import ru.nsu.ermakov.graph.core.Edge;
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
 * Реализация графа через матрицу инцидентности.
 * Строки соответствуют вершинам, столбцы соответствуют рёбрам.
 * Для ориентированного ребра (u -> v):
 *  - в строке вершины u стоит -1 в соответствующем столбце;
 *  - в строке вершины v стоит +1 в этом столбце;
 *  - во всех остальных строках 0.
 * Массив edges[col] хранит само ребро (u -> v) для столбца col.
 */
public class IncidenceMatrixGraph extends AbstractGraph {

    private final ArrayList<Integer> vertices = new ArrayList<>();
    private final Map<Integer, Integer> indexOf = new HashMap<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private int[][] incMatrix = new int[0][0];

    @Override
    public boolean addVertex(int v) {
        if (indexOf.containsKey(v)) {
            return false;
        }

        int oldRows = vertices.size();
        int newRows = oldRows + 1;
        int cols = edges.size();

        vertices.add(v);
        indexOf.put(v, oldRows);

        int[][] newMat = new int[newRows][cols];
        for (int r = 0; r < oldRows; r++) {
            System.arraycopy(incMatrix[r], 0, newMat[r], 0, cols);
        }

        // новая строка пока вся нулевая
        incMatrix = newMat;
        return true;
    }

    @Override
    public boolean removeVertex(int v) {
        Integer idxObj = indexOf.get(v);
        if (idxObj == null) {
            return false;
        }

        int rowToRemove = idxObj;
        int oldRows = vertices.size();
        int oldCols = edges.size();

        // выясняем, какие столбцы (рёбра) инцидентны удаляемой вершине
        boolean[] removeCol = new boolean[oldCols];
        int newColsCount = 0;

        for (int c = 0; c < oldCols; c++) {
            if (incMatrix[rowToRemove][c] != 0) {
                removeCol[c] = true;
            } else {
                removeCol[c] = false;
                newColsCount++;
            }
        }

        // соберём новый список рёбер без удалённых
        ArrayList<Edge> newEdges = new ArrayList<>(newColsCount);
        for (int c = 0; c < oldCols; c++) {
            if (!removeCol[c]) {
                newEdges.add(edges.get(c));
            }
        }

        int newRows = oldRows - 1;
        int newCols = newColsCount;
        int[][] newMat = new int[newRows][newCols];

        // копируем всё, пропуская удаляемую строку и удалённые столбцы
        for (int rOld = 0, rNew = 0; rOld < oldRows; rOld++) {
            if (rOld == rowToRemove) {
                continue;
            }
            for (int cOld = 0, cNew = 0; cOld < oldCols; cOld++) {
                if (!removeCol[cOld]) {
                    newMat[rNew][cNew] = incMatrix[rOld][cOld];
                    cNew++;
                }
            }
            rNew++;
        }

        vertices.remove(rowToRemove);
        indexOf.clear();
        for (int i = 0; i < vertices.size(); i++) {
            indexOf.put(vertices.get(i), i);
        }

        edges.clear();
        edges.addAll(newEdges);
        incMatrix = newMat;
        return true;
    }

    @Override
    public boolean hasVertex(int v) {
        return indexOf.containsKey(v);
    }

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

        // проверяем, нет ли уже такого же ребра
        for (Edge e : edges) {
            if (e.getFrom() == from && e.getTo() == to) {
                return false;
            }
        }

        int rows = vertices.size();
        int oldCols = edges.size();
        int newCols = oldCols + 1;

        int[][] newMat = new int[rows][newCols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(incMatrix[r], 0, newMat[r], 0, oldCols);
        }

        // заполняем новый столбец: -1 в источнике, +1 в приёмнике
        newMat[fromIdx][oldCols] = -1;
        newMat[toIdx][oldCols] = 1;

        edges.add(new Edge(from, to));
        incMatrix = newMat;
        return true;
    }

    @Override
    public boolean removeEdge(int from, int to) {
        int colToRemove = -1;
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if (e.getFrom() == from && e.getTo() == to) {
                colToRemove = i;
                break;
            }
        }

        if (colToRemove < 0) {
            return false;
        }

        int rows = vertices.size();
        int oldCols = edges.size();
        int newCols = oldCols - 1;

        int[][] newMat = new int[rows][newCols];
        for (int r = 0; r < rows; r++) {
            for (int cOld = 0, cNew = 0; cOld < oldCols; cOld++) {
                if (cOld == colToRemove) {
                    continue;
                }
                newMat[r][cNew] = incMatrix[r][cOld];
                cNew++;
            }
        }

        edges.remove(colToRemove);
        incMatrix = newMat;
        return true;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        for (Edge e : edges) {
            if (e.getFrom() == from && e.getTo() == to) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Integer> getVertices() {
        return Collections.unmodifiableSet(
                new LinkedHashSet<>(vertices)
        );
    }

    @Override
    public Set<Integer> getNeighbors(int v) {
        Integer rowObj = indexOf.get(v);
        if (rowObj == null) {
            throw new IllegalArgumentException(
                    "Vertex " + v + " does not exist"
            );
        }

        int row = rowObj;
        LinkedHashSet<Integer> neigh = new LinkedHashSet<>();

        // для каждого столбца, где эта вершина является источником (-1),
        // найдём вершину-приёмник (+1)
        for (int c = 0; c < edges.size(); c++) {
            if (incMatrix[row][c] == -1) {
                for (int r = 0; r < vertices.size(); r++) {
                    if (incMatrix[r][c] == 1) {
                        neigh.add(vertices.get(r));
                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableSet(neigh);
    }

    @Override
    public void clear() {
        vertices.clear();
        indexOf.clear();
        edges.clear();
        incMatrix = new int[0][0];
    }

    @Override
    public void loadFromFile(Path path) throws IOException {
        GraphFileLoader.load(this, path);
    }
    @Override
    public void sort() {
        if (edges != null) {
            Arrays.sort(edges, Comparator.comparingDouble(Edge::getWeight));
        }
    }

}
