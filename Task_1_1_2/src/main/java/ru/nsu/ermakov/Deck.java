package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Создает одну или несколько колод по 52 карты.
     *
     * @param numDecks количество колод
     */
    public Deck(int numDecks) {
        for (int n = 0; n < numDecks; n++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }
        shuffle();
    }

    /**
     * Перемешивает колоду.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Берёт верхнюю карту из колоды.
     *
     * @return карта
     */
    public Card draw() {
        return cards.remove(cards.size() - 1);
    }
}
