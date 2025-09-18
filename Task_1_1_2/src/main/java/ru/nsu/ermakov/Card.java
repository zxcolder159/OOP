package ru.nsu.ermakov;

/**
 * Класс, представляющий карту в игре Блэкджек.
 */
public class Card {
    private final String suit;
    private final String rank;

    /**
     * Конструктор карты.
     *
     * @param rank номинал карты (2–10, Валет, Дама, Король, Туз)
     * @param suit масть карты (Пики, Червы, Бубны, Трефы)
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Возвращает номинал карты.
     */
    public String getRank() {
        return rank;
    }

    /**
     * Возвращает масть карты.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Возвращает базовое значение карты.
     * Для туза всегда 11, коррекция до 1 выполняется в Player.getScore().
     */
    public int getBaseValue() {
        if (rank.matches("\\d+")) {
            return Integer.parseInt(rank);
        } else if ("Туз".equals(rank)) {
            return 11;
        } else {
            return 10;
        }
    }

    @Override
    public String toString() {
        return rank + " " + suit;
    }
}
