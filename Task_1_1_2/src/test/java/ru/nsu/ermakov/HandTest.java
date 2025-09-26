package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {
    @Test
    void testAddCardAndClear() {
        Hand hand = new Hand();
        Card card = new Card(Rank.ACE, Suit.SPADES);
        hand.addCard(card);

        assertFalse(hand.getCards().isEmpty());
        assertEquals(1, hand.getCards().size());

        hand.clear();
        assertTrue(hand.getCards().isEmpty());
    }
}
