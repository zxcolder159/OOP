package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {
    private final int numDecks;
    private List<Card> cards;

    /**
     * Создаёт одну или несколько колод по 52 карты.
     *
     * @param numDecks количество колод
     */
    public Deck(int numDecks) {
        this.numDecks = numDecks;
        this.cards = createDeck(numDecks);
        shuffle();
    }

    /**
     * Создаёт колоду из указанного количества колод.
     */
    private List<Card> createDeck(int numDecks) {
        List<Card> newDeck = new ArrayList<>();
        for (int n = 0; n < numDecks; n++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    newDeck.add(new Card(rank, suit));
                }
            }
        }
        return newDeck;
    }

    /**
     * Перемешивает колоду.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Берёт верхнюю карту из колоды.
     * Если колода пуста — создаётся новая и тасуется.
     *
     * @return карта
     */
    public Card draw() {
        if (cards.isEmpty()) {
            System.out.println("Колода закончилась! Создаётся новая колода...");
            cards = createDeck(numDecks);
            shuffle();
        }
        return cards.remove(cards.size() - 1);
    }
}
