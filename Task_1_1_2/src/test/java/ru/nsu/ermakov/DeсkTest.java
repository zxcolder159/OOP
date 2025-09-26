package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DeckTest {
    @Test
    void testDeckCreationAndDraw() {
        Deck deck = new Deck(1);
        assertNotNull(deck.draw());
    }

    @Test
    void testDeckReshuffleWhenEmpty() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 60; i++) {
            deck.draw();
        }
        assertNotNull(deck.draw());
    }
}
