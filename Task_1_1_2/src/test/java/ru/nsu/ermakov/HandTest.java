package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void testScore() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.ПИКИ));
        hand.addCard(new Card(Rank.NINE, Suit.БУБНЫ));
        assertEquals(20, hand.getScore());
    }

    @Test
    void testToString() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.ПИКИ));
        String result = hand.toString(false);
        assertEquals(true, result.contains("Туз"));
    }
}
