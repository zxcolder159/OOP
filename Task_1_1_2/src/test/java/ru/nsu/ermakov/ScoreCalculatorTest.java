package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculatorTest {
    @Test
    void testCalculateWithAce() {
        List<Card> cards = List.of(new Card(Rank.ACE, Suit.ПИКИ),
                new Card(Rank.NINE, Suit.БУБНЫ));
        assertEquals(20, ScoreCalculator.calculate(cards));
    }

    @Test
    void testCalculateWithMultipleAces() {
        List<Card> cards = List.of(new Card(Rank.ACE, Suit.ПИКИ),
                new Card(Rank.ACE, Suit.БУБНЫ),
                new Card(Rank.NINE, Suit.ТРЕФЫ));
        assertEquals(21, ScoreCalculator.calculate(cards));
    }
}
