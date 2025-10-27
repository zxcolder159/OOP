package ru.nsu.ermakov.graph.core;

import java.util.Objects;

/**
 * Неизменяемое ориентированное ребро (from -> to).
 * Хранит начало и конец дуги в виде целых чисел.
 */
public final class Edge implements Comparable<Edge> {

    private final int from;
    private final int to;

    /**
     * Создаёт ребро (from -> to).
     */
    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Возвращает начальную вершину ребра.
     * @return номер вершины-источника.
     */
    public int getFrom() {
        return from;
    }

    /**
     * Возвращает конечную вершину ребра.
     * @return номер вершины-приёмника.
     */
    public int getTo() {
        return to;
    }

    /**
     * Сравнивает два ребра лексикографически: сначала по полю from,
     * затем по полю to.
     * @return отрицательное число, ноль или положительное число,
     *     если это ребро меньше, равно или больше другого
     */
    @Override
    public int compareTo(Edge other) {
        if (from != other.from) {
            return Integer.compare(from, other.from);
        }
        return Integer.compare(to, other.to);
    }

    /**
     * Проверяет равенство двух рёбер.
     * Два ребра равны, если совпадают оба конца (from и to).
     * @return true, если ребро эквивалентно, иначе false
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) other;
        return from == e.from && to == e.to;
    }

    /**
     * Возвращает хэш-код ребра.
     * Хэш-код вычисляется на основе полей from и to.
     * @return хэш-код ребра
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    /**
     * Возвращает строковое представление ребра в формате "from->to".
     * @return строка с направлением ребра
     */
    @Override
    public String toString() {
        return from + "->" + to;
    }
}
