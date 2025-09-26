package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DealerTest {
    @Test
    void testCompareWithPlayer_PlayerWin() {
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.FIVE, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.KING, Suit.ПИКИ));

        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_DealerWin() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.FIVE, Suit.ПИКИ));

        assertEquals(GameResult.DEALER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_Draw() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.TEN, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.TEN, Suit.ПИКИ));

        assertEquals(GameResult.DRAW, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_PlayerBust() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        player.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.QUEEN, Suit.ПИКИ));
        player.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        assertEquals(GameResult.DEALER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_DealerBust() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        dealer.takeCard(new Card(Rank.QUEEN, Suit.ПИКИ));
        dealer.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        player.takeCard(new Card(Rank.NINE, Suit.ТРЕФЫ));

        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }
}
