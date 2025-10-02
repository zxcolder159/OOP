package ru.nsu.ermakov.Kartishki;

import ru.nsu.ermakov.Enums.Rank;
import ru.nsu.ermakov.Enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, представляющий колоду карт.
 */
public class Deck {
    private final int numDecks;
    private List<InputHandler.Card> cards;

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
    private List<InputHandler.Card> createDeck(int numDecks) {
        List<InputHandler.Card> newDeck = new ArrayList<>();
        for (int n = 0; n < numDecks; n++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    newDeck.add(new InputHandler.Card(rank, suit));
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
    public InputHandler.Card draw() {
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