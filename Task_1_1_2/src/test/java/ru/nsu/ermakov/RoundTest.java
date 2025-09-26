package ru.nsu.ermakov;

import java.util.Scanner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundTest {
    @Test
    void testPlayerBust() {
        GameStats stats = new GameStats();
        Scanner scanner = new Scanner("1\n");
        Round round = new Round(1, stats, scanner);

        Player player = new Player("Игрок");
        Dealer dealer = new Dealer("Дилер");

        player.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.QUEEN, Suit.ПИКИ));
        player.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        assertEquals(true, player.isBusted());

        stats.addDealerWin();

        assertEquals(1, stats.getDealerWins());
        assertEquals(0, stats.getPlayerWins());
    }
}
