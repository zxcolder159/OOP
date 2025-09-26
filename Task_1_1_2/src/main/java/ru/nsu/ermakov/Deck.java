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
     * Создаёт одну или несколько колод из 52 карт.
     *
     * @param numDecks количество колод
     */
    public Deck(int numDecks) {
        this.numDecks = numDecks;
        this.cards = createDeck(numDecks);
        shuffle();
    }

    /**
     * Создание новой колоды.
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
     * Если карты закончились — создаёт новую колоду.
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

    /**
     * Возвращает количество оставшихся карт.
     *
     * @return размер колоды
     */
    public int size() {
        return cards.size();
    }
}
