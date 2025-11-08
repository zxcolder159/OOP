package ru.nsu.ermakov.graph.core;

import java.util.ArrayList;
import java.util.List;

public interface TopologicalSortStrategy {
    List<Integer> topologicalSort(Graph graph);
}