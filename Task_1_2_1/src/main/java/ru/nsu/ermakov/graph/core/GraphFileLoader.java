package ru.nsu.ermakov.graph.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Утилита чтения графа из файла фиксированного формата.
 *
 * Формат файла:
 * 1) Первая строка: n m
 *    (число вершин и число рёбер)
 * 2) Вторая строка: v1 v2 ... vN
 *    (id всех вершин)
 * 3) Далее m строк вида: a b
 *    (ребро a -> b)
 *
 * Перед загрузкой вызывается graph.clear().
 */
public final class GraphFileLoader {

    private GraphFileLoader() {
        // utility class
    }

    /**
     * Считать данные из файла и заполнить указанный graph.
     *
     * @param graph граф, в который грузим
     * @param path путь к файлу
     * @throws IOException при ошибке чтения или формата
     */
    public static void load(Graph graph, Path path) throws IOException {
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.size() < 2) {
            throw new IOException("Bad format: not enough lines");
        }

        // ---- line 1: n m ----
        StringTokenizer headerTokenizer = new StringTokenizer(lines.get(0));
        if (headerTokenizer.countTokens() < 2) {
            throw new IOException("Bad format in line 1: expected n m");
        }
        int n = Integer.parseInt(headerTokenizer.nextToken());
        int m = Integer.parseInt(headerTokenizer.nextToken());

        // ---- line 2: list of vertices ----
        StringTokenizer verticesTokenizer = new StringTokenizer(lines.get(1));
        if (verticesTokenizer.countTokens() < n) {
            throw new IOException(
                    "Bad format in line 2: expected " + n + " vertices"
            );
        }

        graph.clear();

        for (int i = 0; i < n; i++) {
            int v = Integer.parseInt(verticesTokenizer.nextToken());
            graph.addVertex(v);
        }

        // ---- next m lines: edges ----
        if (lines.size() < 2 + m) {
            throw new IOException("Bad format: not enough edge lines");
        }

        for (int i = 0; i < m; i++) {
            String line = lines.get(2 + i);
            StringTokenizer edgeTokenizer = new StringTokenizer(line);
            if (edgeTokenizer.countTokens() < 2) {
                throw new IOException("Bad format in edge line: " + line);
            }
            int from = Integer.parseInt(edgeTokenizer.nextToken());
            int to = Integer.parseInt(edgeTokenizer.nextToken());
            graph.addEdge(from, to);
        }
    }
}
