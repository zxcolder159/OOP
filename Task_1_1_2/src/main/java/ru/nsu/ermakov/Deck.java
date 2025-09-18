package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    private static final String[] SUITS = {"Пики", "Червы", "Бубны", "Трефы"};
    private static final String[] RANKS = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "Валет", "Дама", "Король", "Туз"
    };

    /**
     * Создает одну или несколько колод из 52 карт.
     */
    public Deck(int numDecks) {
        for (int n = 0; n < numDecks; n++) {
            for (String suit : SUITS) {
                for (String rank : RANKS) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        shuffle();
    }

    /** Перемешивает колоду. */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /** Берет верхнюю карту из колоды. */
    public Card draw() {
        return cards.remove(cards.size() - 1);
    }
}
