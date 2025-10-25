package ru.nsu.ermakov.graph.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

/**
 * Интерфейс ориентированного графа.
 *
 * Операции:
 * 1. Добавление / удаление вершины.
 * 2. Добавление / удаление ребра.
 * 3. Получение соседей вершины (исходящих).
 * 4. Чтение графа из файла фиксированного формата.
 * 5. Вспомогательные операции.
 */
public interface Graph {
    /**
     * Добавляет вершину с данным id.
     *
     * @param v id вершины
     * @return true, если вершина была добавлена; false, если уже существовала
     */
    boolean addVertex(int v);

    /**
     * Удаляет вершину с данным id (и все инцидентные рёбра).
     *
     * @param v id вершины
     * @return true, если вершина существовала и была удалена
     */
    boolean removeVertex(int v);

    /**
     * Проверяет наличие вершины.
     *
     * @param v id вершины
     * @return true, если вершина существует
     */
    boolean hasVertex(int v);

    /**
     * Добавляет ориентированное ребро (from -> to).
     * Оба конца ребра должны существовать.
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return true, если ребро добавлено; false, если оно уже было
     * @throws IllegalArgumentException если одной из вершин нет в графе
     */
    boolean addEdge(int from, int to);

    /**
     * Удаляет ориентированное ребро (from -> to).
     *
     * @param from начало ребра
     * @param to конец ребра
     * @return true, если ребро существовало и было удалено
     */
    boolean removeEdge(int from, int to);

    /**
     * Проверяет существование ребра (from -> to).
     *
     * @param from начало
     * @param to конец
     * @return true, если такое ребро есть
     */
    boolean hasEdge(int from, int to);

    /**
     * Возвращает множество всех вершин графа.
     * Возвращаемое множество не должно позволять менять внутреннее состояние.
     *
     * @return множество id вершин
     */
    Set<Integer> getVertices();

    /**
     * Возвращает множество соседей вершины v:
     * вершин, в которые ведут исходящие рёбра из v.
     *
     * @param v вершина-источник
     * @return множество id соседей
     * @throws IllegalArgumentException если вершины v нет
     */
    Set<Integer> getNeighbors(int v);

    /**
     * @return количество вершин.
     */
    int getVertexCount();

    /**
     * @return количество рёбер.
     */
    int getEdgeCount();

    /**
     * Полностью очистить граф (удалить все вершины и рёбра).
     */
    void clear();

    /**
     * Загрузить граф из файла фиксированного формата.
     * Формат описан в {@link GraphFileLoader}.
     * Перед загрузкой вызывается clear().
     *
     * @param path путь к файлу
     * @throws IOException при ошибке чтения или неверном формате
     */
    void loadFromFile(Path path) throws IOException;
}
