package ru.nsu.ermakov.Enums;

/**
 * Перечисление номиналов карт.
 */
public enum Rank {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    JACK("Валет", 10),
    QUEEN("Дама", 10),
    KING("Король", 10),
    ACE("Туз", 11);

    private final String name;
    private final int value;

    Rank(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Название номинала.
     */
    public String getName() {
        return name;
    }

    /**
     * Очковое значение номинала.
     */
    public int getValue() {
        return value;
    }
}
