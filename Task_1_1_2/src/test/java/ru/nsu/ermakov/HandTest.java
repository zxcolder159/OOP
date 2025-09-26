package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {
    @Test
    void testAddAndClear() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN, Suit.ПИКИ));
        assertEquals(1, hand.getCards().size());
        hand.clear();
        assertEquals(0, hand.getCards().size());
    }

    @Test
    void testScoreWithAceAdjustment() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.БУБНЫ));
        hand.addCard(new Card(Rank.KING, Suit.ТРЕФЫ));
        assertEquals(21, hand.getScore());
    }

    @Test
    void testToStringHideFirst() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.TEN, Suit.ПИКИ));
        hand.addCard(new Card(Rank.FIVE, Suit.БУБНЫ));
        String str = hand.toString(true);
        assertTrue(str.contains("<закрытая карта>"));
    }
}
