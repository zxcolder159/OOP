package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {
    @Test
    void testTakeCardAndClear() {
        Player player = new Player("Игрок");
        player.takeCard(new Card(Rank.TEN, Suit.БУБНЫ));
        assertEquals(1, player.getCards().size());
        player.clearHand();
        assertEquals(0, player.getCards().size());
    }

    @Test
    void testIsBusted() {
        Player player = new Player("Игрок");
        player.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.QUEEN, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.JACK, Suit.БУБНЫ));
        assertTrue(player.isBusted());
    }
}
