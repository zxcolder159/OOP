package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {
    @Test
    void testCompareWithPlayerWinLoseDraw() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.ПИКИ));
        player.takeCard(new Card(Rank.ACE, Suit.БУБНЫ));
        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }
}
