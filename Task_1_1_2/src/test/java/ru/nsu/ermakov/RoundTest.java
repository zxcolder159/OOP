package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundTest {
    @Test
    void testPlayerBust() {
        GameStats stats = new GameStats();
        Scanner scanner = new Scanner("1\n1\n1\n0\n");
        Round round = new Round(1, stats, scanner);
        round.play();

        assertEquals(1, stats.getDealerWins());
    }

    @Test
    void testPlayerStand() {
        GameStats stats = new GameStats();
        Scanner scanner = new Scanner("0\n");
        Round round = new Round(1, stats, scanner);
        round.play();

        int total = stats.getPlayerWins() + stats.getDealerWins();
        assertEquals(true, total >= 1);
    }
}
