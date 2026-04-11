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
public final class TopologicalSorter implements TopologicalSortStrategy {
    @Override
    public List<Integer> topologicalSort(Graph graph) {
        Map<Integer, Integer> indegree = new HashMap<>();
        for (Integer v : graph.getVertices()) {
            indegree.put(v, 0);
        }
        for (Integer v : graph.getVertices()) {
            for (Integer to : graph.getNeighbors(v)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (Map.Entry<Integer, Integer> entry : indegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Integer vertex = queue.poll();
            result.add(vertex);
            for (Integer neighbor : graph.getNeighbors(vertex)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (result.size() != graph.getVertices().size()) {
            throw new IllegalStateException("Граф содержит цикл");
        }
        return result;
    }
}

