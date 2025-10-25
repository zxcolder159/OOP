package ru.nsu.ermakov.graph.core;

import java.util.Objects;

/**
 * Неизменяемое ориентированное ребро (from -> to).
 */
public final class Edge implements Comparable<Edge> {
    private final int from;
    private final int to;

    /**
     * Создаёт ребро (from -> to).
     *
     * @param from начало
     * @param to конец
     */
    public Edge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * @return начало ребра.
     */
    public int getFrom() {
        return from;
    }

    /**
     * @return конец ребра.
     */
    public int getTo() {
        return to;
    }

    @Override
    public int compareTo(Edge other) {
        if (from != other.from) {
            return Integer.compare(from, other.from);
        }
        return Integer.compare(to, other.to);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return from + "->" + to;
    }
}
