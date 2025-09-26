package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    @Test
    void testAddAndClear() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.ПИКИ));
        assertEquals(1, hand.getCards().size());
        hand.clear();
        assertEquals(0, hand.getCards().size());
    }

    @Test
    void testGetScoreWithAce() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.ПИКИ));
        hand.addCard(new Card(Rank.KING, Suit.БУБНЫ));
        assertEquals(21, hand.getScore());
    }

    @Test
    void testToString() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.FIVE, Suit.ТРЕФЫ));
        assertTrue(hand.toString(false).contains("5"));
    }
}
