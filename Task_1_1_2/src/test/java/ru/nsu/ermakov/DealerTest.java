package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.FIVE, Suit.ПИКИ));

        assertEquals(GameResult.DEALER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_Draw() {
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.TEN, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.TEN, Suit.ПИКИ));

        assertEquals(GameResult.DRAW, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_PlayerBust() {
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");

        player.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        player.takeCard(new Card(Rank.QUEEN, Suit.ПИКИ));
        player.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        assertEquals(GameResult.DEALER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testCompareWithPlayer_DealerBust() {
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.БУБНЫ));
        dealer.takeCard(new Card(Rank.QUEEN, Suit.ПИКИ));
        dealer.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        player.takeCard(new Card(Rank.NINE, Suit.ТРЕФЫ));

        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testDealInitialCards() {
        final Dealer dealer = new Dealer("Дилер");
        final Player player = new Player("Игрок");
        final Deck deck = new Deck(1);

        dealer.dealInitialCards(player, deck);

        assertEquals(2, dealer.getCards().size());
        assertEquals(2, player.getCards().size());
    }

    @Test
    void testPlayTurn_NoExtraDraw() {
        final Dealer dealer = new Dealer("Дилер");
        final Deck deck = new Deck(1);

        dealer.takeCard(new Card(Rank.TEN, Suit.БУБНЫ));
        dealer.takeCard(new Card(Rank.NINE, Suit.ПИКИ));

        dealer.playTurn(deck);

        assertTrue(dealer.getScore() >= 17);
        assertEquals(2, dealer.getCards().size());
    }

    @Test
    void testPlayTurn_DrawUntil17() {
        final Dealer dealer = new Dealer("Дилер");
        final Deck deck = new Deck(1);

        dealer.takeCard(new Card(Rank.TEN, Suit.БУБНЫ));

        dealer.playTurn(deck);

        assertTrue(dealer.getScore() >= 17);
        assertTrue(dealer.getCards().size() >= 2);
    }
}
