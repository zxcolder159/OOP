package ru.nsu.ermakov.Game;

import ru.nsu.ermakov.Enums.Rank;
import ru.nsu.ermakov.Enums.Suit;

/**
 * Класс, представляющий карту в игре Блэкджек.
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    /**
     * Конструктор карты.
     *
     * @param rank номинал
     * @param suit масть
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Возвращает номинал карты.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает масть карты.
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает базовое значение карты.
     */
    public int getBaseValue() {
        return rank.getValue();
    }

    @Override
    public String toString() {
        return rank.getName() + " " + suit.getName();
    }
}