package ru.nsu.ermakov;

import ru.nsu.ermakov.graph.core.Graph;
import ru.nsu.ermakov.graph.core.TopologicalSorter;
import ru.nsu.ermakov.graph.impl.AdjacencyListGraph;
import ru.nsu.ermakov.graph.impl.AdjacencyMatrixGraph;
import ru.nsu.ermakov.graph.impl.IncidenceMatrixGraph;
import java.nio.file.Paths;
import java.util.List;

/**
 * Пример использования различных реализаций графа и алгоритма топологической сортировки.
 */
public final class Main {

    /**
     * Приватный конструктор утилитного класса.
     */
    private Main() {
        // utility
    }

    /**
     * Точка входа в демонстрационную программу.
     */
    public static void main(String[] args) throws Exception {
        // 1. Создаём граф на списке смежности
        Graph g1 = new AdjacencyListGraph();
        g1.addVertex(10);
        g1.addVertex(20);
        g1.addVertex(30);

        g1.addEdge(10, 20);
        g1.addEdge(10, 30);

        System.out.println("g1 = " + g1);

        // Используем объект TopologicalSorter для сортировки
        TopologicalSorter sorter = new TopologicalSorter();
        List<Integer> order = sorter.topologicalSort(g1);
        System.out.println("toposort(g1) = " + order);

        // 2. Загружаем граф из файла в другую реализацию (матрица смежности)
        Graph g2 = new AdjacencyMatrixGraph();
        g2.loadFromFile(Paths.get("graph.txt"));  // Убедитесь, что файл graph.txt существует
        System.out.println("g2 = " + g2);

        // 3. Сравним графы по структуре
        System.out.println("g1 equals g2? " + g1.equals(g2));

        // 4. Загрузим тот же файл в матрицу инцидентности
        Graph g3 = new IncidenceMatrixGraph();
        g3.loadFromFile(Paths.get("graph.txt"));  // Убедитесь, что файл graph.txt существует
        System.out.println("g3 = " + g3);

        // 5. Проверим топологическую сортировку и для g3
        List<Integer> orderG3 = sorter.topologicalSort(g3);
        System.out.println("toposort(g3) = " + orderG3);
    }
}
