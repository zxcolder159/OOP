package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void testCardProperties() {
        Card card = new Card(Rank.ACE, Suit.ПИКИ);
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(Suit.ПИКИ, card.getSuit());
        assertEquals(11, card.getBaseValue());
    }

    @Test
    void testToString() {
        Card card = new Card(Rank.KING, Suit.БУБНЫ);
        String str = card.toString();
        assertTrue(str.contains("Король"));
        assertTrue(str.contains("Бубны"));
    }
}
