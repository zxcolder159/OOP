package ru.nsu.ermakov.graph.core;

import java.util.ArrayList;

public interface TopologicalSortStrategy {
    List<Integer> topologicalSort(Graph graph);
}