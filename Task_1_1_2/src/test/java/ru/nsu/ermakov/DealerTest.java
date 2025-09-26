package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DealerTest {
    @Test
    void testDealInitialCards() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");
        Deck deck = new Deck(1);

        dealer.dealInitialCards(player, deck);

        assertEquals(2, dealer.getCards().size());
        assertEquals(2, player.getCards().size());
    }

    @Test
    void testCompareWithPlayer() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        player.takeCard(new Card(Rank.KING, Suit.ПИКИ));
        dealer.takeCard(new Card(Rank.QUEEN, Suit.БУБНЫ));

        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testDealerBust() {
        Dealer dealer = new Dealer("Дилер");
        Player player = new Player("Игрок");

        dealer.takeCard(new Card(Rank.KING, Suit.ПИКИ));
        dealer.takeCard(new Card(Rank.QUEEN, Suit.БУБНЫ));
        dealer.takeCard(new Card(Rank.JACK, Suit.ТРЕФЫ));

        assertTrue(dealer.isBusted());
        assertEquals(GameResult.PLAYER_WIN, dealer.compareWithPlayer(player));
    }

    @Test
    void testDealerNotBust() {
        Dealer dealer = new Dealer("Дилер");
        dealer.takeCard(new Card(Rank.SEVEN, Suit.ПИКИ));
        dealer.takeCard(new Card(Rank.NINE, Suit.БУБНЫ));

        assertFalse(dealer.isBusted());
    }
}
