package ru.nsu.ermakov;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
