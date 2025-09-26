package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeckTest {
    @Test
    void testDraw() {
        Deck deck = new Deck(1);
        Card card = deck.draw();
        assertNotNull(card);
    }

    @Test
    void testReshuffleWhenEmpty() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }
        Card newCard = deck.draw();
        assertNotNull(newCard);
    }

    @Test
    void testDeckSizeAfterDraw() {
        Deck deck = new Deck(1);
        int before = deck.size();
        deck.draw();
        assertEquals(before - 1, deck.size());
    }
}
