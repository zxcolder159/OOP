package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    void testDeckSize() {
        Deck deck = new Deck(1);
        assertEquals(52, deck.size());
    }

    @Test
    void testDeckDraw() {
        Deck deck = new Deck(1);
        Card card = deck.draw();
        assertNotNull(card);
        assertEquals(51, deck.size());
    }

    // --- Player ---
    @Test
    void testPlayerScoreSimple() {
        Player p = new Player("Игрок");
        p.addCard(new Card("10", "Пики"));
        assertEquals(10, p.getScore());
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
    void testShowHand() {
        Player p = new Player("Игрок");
        p.addCard(new Card("10", "Пики"));
        p.addCard(new Card("Дама", "Червы"));
        assertTrue(p.showHand(false).contains("10 Пики"));
        assertTrue(p.showHand(true).contains("<закрытая карта>"));
    }

    // --- Blackjack ---
    @Test
    void testBlackjackOneRoundStopImmediately() {
        String input = "0\n"; // игрок сразу останавливается
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Blackjack game = new Blackjack(in, new PrintStream(out));
        game.playRound();

        String output = out.toString();
        assertTrue(output.contains("Ваши карты"));
        assertTrue(output.contains("Карты дилера"));
    }

    @Test
    void testBlackjackStart() {
        String input = "0\nn\n"; // игрок сразу останавливается и выходит
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Blackjack game = new Blackjack(in, new PrintStream(out));
        game.start();

        String output = out.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Раунд 1"));
    }
}
