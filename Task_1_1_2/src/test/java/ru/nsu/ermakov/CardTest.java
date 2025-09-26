package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    @Test
    void testGetters() {
        Card card = new Card(Rank.ACE, Suit.ПИКИ);
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.ПИКИ, card.getSuit());
    }

    @Test
    void testBaseValue() {
        Card card = new Card(Rank.KING, Suit.БУБНЫ);
        assertEquals(10, card.getBaseValue());
    }

    @Test
    void testToString() {
        Card card = new Card(Rank.JACK, Suit.ТРЕФЫ);
        String result = card.toString();
        assertTrue(result.contains("Валет"));
        assertTrue(result.contains("Трефы"));
    }
}
