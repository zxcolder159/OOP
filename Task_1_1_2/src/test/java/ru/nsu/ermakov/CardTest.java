package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    @Test
    void testGetRankAndSuit() {
        Card card = new Card(Rank.ACE, Suit.ПИКИ);
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.ПИКИ, card.getSuit());
    }

    @Test
    void testGetBaseValue() {
        assertEquals(11, new Card(Rank.ACE, Suit.БУБНЫ).getBaseValue());
        assertEquals(10, new Card(Rank.KING, Suit.ТРЕФЫ).getBaseValue());
        assertEquals(7, new Card(Rank.SEVEN, Suit.ЧЕРВЫ).getBaseValue());
    }

    @Test
    void testToString() {
        Card card = new Card(Rank.JACK, Suit.БУБНЫ);
        assertTrue(card.toString().contains("Валет"));
    }
}
