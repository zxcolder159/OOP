package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Юнит-тесты для классов Card, Deck и Player.
 */
class GameTest {

    // ---------- Card ----------
    @Test
    void testNumberCardValue() {
        Card card = new Card("7", "Пики");
        assertEquals(7, card.getBaseValue());
    }

    @Test
    void testFaceCardValue() {
        Card card = new Card("Король", "Червы");
        assertEquals(10, card.getBaseValue());
    }

    @Test
    void testAceCardValue() {
        Card card = new Card("Туз", "Бубны");
        assertEquals(11, card.getBaseValue());
    }

    @Test
    void testCardToString() {
        Card card = new Card("Дама", "Трефы");
        assertEquals("Дама Трефы", card.toString());
    }

    // ---------- Deck ----------
    @Test
    void testDeckSizeSingle() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.draw());
        }
    }

    @Test
    void testDeckSizeMultiple() {
        Deck deck = new Deck(2);
        for (int i = 0; i < 104; i++) {
            assertNotNull(deck.draw());
        }
    }

    @Test
    void testShuffle() {
        Deck deck = new Deck(1);
        deck.shuffle();
        assertNotNull(deck.draw()); // хотя бы карта есть после shuffle
    }

    // ---------- Player ----------
    @Test
    void testAddAndClearHand() {
        Player player = new Player("Игрок");
        player.addCard(new Card("10", "Пики"));
        assertEquals(10, player.getScore());
        player.clearHand();
        assertEquals(0, player.getScore());
    }

    @Test
    void testScoreWithAceAs11() {
        Player player = new Player("Игрок");
        player.addCard(new Card("Туз", "Пики"));
        player.addCard(new Card("9", "Червы"));
        assertEquals(20, player.getScore());
    }

    @Test
    void testScoreWithAceAs1() {
        Player player = new Player("Игрок");
        player.addCard(new Card("Туз", "Пики"));
        player.addCard(new Card("9", "Червы"));
        player.addCard(new Card("5", "Бубны"));
        assertEquals(15, player.getScore()); // туз = 1
    }

    @Test
    void testMultipleAces() {
        Player player = new Player("Игрок");
        player.addCard(new Card("Туз", "Пики"));
        player.addCard(new Card("Туз", "Червы"));
        player.addCard(new Card("9", "Бубны"));
        assertEquals(21, player.getScore());
    }

    @Test
    void testShowHandWithoutHidden() {
        Player player = new Player("Игрок");
        player.addCard(new Card("10", "Пики"));
        player.addCard(new Card("Дама", "Червы"));
        String result = player.showHand(false);
        assertTrue(result.contains("10 Пики"));
        assertTrue(result.contains("Дама Червы"));
        assertTrue(result.contains("="));
    }

    @Test
    void testShowHandWithHidden() {
        Player player = new Player("Игрок");
        player.addCard(new Card("10", "Пики"));
        player.addCard(new Card("Дама", "Червы"));
        String result = player.showHand(true);
        assertTrue(result.contains("<закрытая карта>"));
    }
    @Test
    void testBlackjackInitialization() {
        Blackjack game = new Blackjack();
        assertNotNull(game);
    }

    @Test
    void testBlackjackOneRoundStopImmediately() throws Exception {
        // эмулируем ввод: "0\n" (сразу остановиться) и "n\n" (не играть снова)
        String input = "0\nn\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Blackjack game = new Blackjack();

        // вызываем приватный метод playRound через reflection
        Method playRound = Blackjack.class.getDeclaredMethod("playRound");
        playRound.setAccessible(true);
        playRound.invoke(game);

        String output = out.toString();
        assertTrue(output.contains("Ваши карты"));
        assertTrue(output.contains("Карты дилера"));
    }

    @Test
    void testBlackjackStartGame() {
        // эмулируем ввод: "0\nn\n" (один раунд, игрок сразу останавливается, потом выход)
        String input = "0\nn\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Blackjack game = new Blackjack();
        game.start();

        String output = out.toString();
        assertTrue(output.contains("Добро пожаловать в Блэкджек!"));
        assertTrue(output.contains("Раунд 1"));
    }
}
