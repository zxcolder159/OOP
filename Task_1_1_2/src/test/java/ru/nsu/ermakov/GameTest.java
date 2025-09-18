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
                new Card("2", "Трефы"),
                new Card("7", "Червы") // запас
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
                new Card("5", "Пики"),
                new Card("8", "Червы") // запас
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
                new Card("10", "Червы"),
                new Card("2", "Пики") // запас
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
                new Card("9", "Трефы"),
                new Card("2", "Червы") // запас
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        assertTrue(out.toString().contains("Ничья"));
    }

    @Test
    void testBlackjackPlayerTakesCardAndStops() {
        Deck deck = fixedDeck(List.of(
                new Card("5", "Пики"),
                new Card("2", "Червы"),
                new Card("5", "Бубны"),
                new Card("3", "Трефы"),
                new Card("9", "Пики"),
                new Card("7", "Червы"),
                new Card("8", "Бубны"),   // запас
                new Card("10", "Пики"),   // запас
                new Card("6", "Червы"),   // запас
                new Card("4", "Трефы")    // запас
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("1\n0\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        String output = out.toString();
        assertTrue(output.contains("Ваши карты"));
        assertTrue(output.contains("Ход дилера"));
    }

    @Test
    void testBlackjackDealerWinsNormally() {
        Deck deck = fixedDeck(List.of(
                new Card("9", "Пики"),
                new Card("10", "Червы"),
                new Card("7", "Бубны"),
                new Card("7", "Трефы"),
                new Card("2", "Червы"), // запас
                new Card("3", "Пики")   // запас
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.playRound();
        String output = out.toString();
        assertTrue(output.contains("Дилер выиграл"));
        assertEquals(1, game.getDealerScoreTotal());
    }

    @Test
    void testBlackjackStartMethodOneRound() {
        Deck deck = fixedDeck(List.of(
                new Card("9", "Пики"),
                new Card("8", "Червы"),
                new Card("7", "Бубны"),
                new Card("6", "Трефы"),
                new Card("2", "Пики"),
                new Card("3", "Червы"),
                new Card("4", "Бубны") // запас
        ));
        ByteArrayInputStream in = new ByteArrayInputStream("0\nn\n".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Blackjack game = new Blackjack(in, new PrintStream(out), deck);
        game.start();
        String output = out.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Раунд 1"));
    }
}
