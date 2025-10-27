package ru.nsu.ermakov.graph.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Топологическая сортировка (алгоритм Канна).
 * Если есть цикл, бросает IllegalStateException.
 */
public final class TopologicalSorter {

    private TopologicalSorter() {
        // utility class
    }

    /**
     * Выполнить топологическую сортировку.
     */
    public static List<Integer> topologicalSort(Graph graph) {
        // Подсчёт входящих степеней.
        Map<Integer, Integer> indegree = new HashMap<>();
        for (Integer v : graph.getVertices()) {
            indegree.put(v, 0);
        }
        for (Integer v : graph.getVertices()) {
            for (Integer to : graph.getNeighbors(v)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        // Приоритетная очередь для детерминированности:
        // всегда берём вершину с минимальным id среди доступных.
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (Map.Entry<Integer, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0) {
                queue.add(e.getKey());
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            Integer v = queue.poll();
            order.add(v);

            for (Integer to : graph.getNeighbors(v)) {
                int newIn = indegree.get(to) - 1;
                indegree.put(to, newIn);
                if (newIn == 0) {
                    queue.add(to);
                }
            }
        }

        if (order.size() != graph.getVertexCount()) {
            throw new IllegalStateException(
                    "Graph has a cycle, topological sort is impossible"
            );
        }

        return order;
    }
}
