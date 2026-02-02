package ru.nsu.ermakov.Enums;

/**
 * Перечисление мастей карт.
 */
public enum Suit {
    ПИКИ("Пики"),
    ЧЕРВЫ("Червы"),
    БУБНЫ("Бубны"),
    ТРЕФЫ("Трефы");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    /**
     * Человекочитаемое название масти.
     */
    public String getName() {
        return name;
    }
}
