package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    // --- Card ---
    @Test
    void testCardValues() {
        assertEquals(7, new Card("7", "Пики").getBaseValue());
        assertEquals(10, new Card("Король", "Червы").getBaseValue());
        assertEquals(11, new Card("Туз", "Бубны").getBaseValue());
    }

    @Test
    void testCardToString() {
        assertEquals("Дама Трефы", new Card("Дама", "Трефы").toString());
    }

    // --- Deck ---
    @Test
    void testDeckSizeAndDraw() {
        Deck deck = new Deck(1);
        assertEquals(52, deck.size());
        Card c = deck.draw();
        assertNotNull(c);
        assertEquals(51, deck.size());
    }

    // --- Player ---
    @Test
    void testPlayerScoreWithoutAce() {
        Player p = new Player("Игрок");
        p.addCard(new Card("10", "Пики"));
        p.addCard(new Card("9", "Червы"));
        assertEquals(19, p.getScore());
    }

    @Test
    void testPlayerScoreWithAceAs11() {
        Player p = new Player("Игрок");
        p.addCard(new Card("Туз", "Пики"));
        p.addCard(new Card("9", "Червы"));
        assertEquals(20, p.getScore());
    }

    @Test
    void testPlayerScoreWithAceAs1() {
        Player p = new Player("Игрок");
        p.addCard(new Card("Туз", "Пики"));
        p.addCard(new Card("9", "Червы"));
        p.addCard(new Card("5", "Бубны"));
        assertEquals(15, p.getScore());
    }

    @Test
    void testPlayerClearHand() {
        Player p = new Player("Игрок");
        p.addCard(new Card("10", "Пики"));
        assertFalse(p.getHand().isEmpty());
        p.clearHand();
        assertTrue(p.getHand().isEmpty());
    }

    @Test
    void testPlayerShowHand() {
        Player p = new Player("Игрок");
        p.addCard(new Card("10", "Пики"));
        p.addCard(new Card("Дама", "Червы"));
        String shown = p.showHand(false);
        assertTrue(shown.contains("10 Пики"));
        assertTrue(shown.contains("= 20"));
        String hidden = p.showHand(true);
        assertTrue(hidden.contains("<закрытая карта>"));
    }

    // --- Blackjack ---
    @Test
    void testBlackjackNatural21() {
        Deck deck = fixedDeck(List.of(
                new Card("Туз", "Пики"),
                new Card("5", "Червы"),
                new Card("Король", "Бубны"),
                new Card("2", "Трефы")
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        assertTrue(out.toString().contains("блэкджек"));
        assertEquals(1, game.getPlayerScoreTotal());
    }

    @Test
    void testBlackjackPlayerBust() {
        Deck deck = fixedDeck(List.of(
                new Card("10", "Пики"),
                new Card("5", "Червы"),
                new Card("9", "Бубны"),
                new Card("2", "Трефы"),
                new Card("5", "Пики")
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        assertTrue(out.toString().contains("Перебор"));
        assertEquals(1, game.getDealerScoreTotal());
    }

    @Test
    void testBlackjackDealerBust() {
        Deck deck = fixedDeck(List.of(
                new Card("10", "Пики"),
                new Card("9", "Червы"),
                new Card("8", "Бубны"),
                new Card("6", "Трефы"),
                new Card("10", "Червы")
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        assertTrue(out.toString().contains("Вы выиграли"));
        assertEquals(1, game.getPlayerScoreTotal());
    }

    @Test
    void testBlackjackDraw() {
        Deck deck = fixedDeck(List.of(
                new Card("10", "Пики"),
                new Card("9", "Червы"),
                new Card("8", "Бубны"),
                new Card("9", "Трефы")
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        assertTrue(out.toString().contains("Ничья"));
    }

    private Deck fixedDeck(List<Card> orderedCards) {
        return new Deck(0) {
            private final List<Card> cards = new ArrayList<>(orderedCards);
            @Override
            public Card draw() {
                return cards.remove(0);
            }
        };
    }
}
