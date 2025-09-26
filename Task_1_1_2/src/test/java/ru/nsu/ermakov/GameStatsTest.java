package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameStatsTest {
    @Test
    void testInitialStats() {
        GameStats stats = new GameStats();
        assertEquals(0, stats.getPlayerWins());
        assertEquals(0, stats.getDealerWins());
        assertEquals(0, stats.getDraws());
    }

    @Test
    void testIncrementStats() {
        GameStats stats = new GameStats();
        stats.incrementPlayerWins();
        stats.incrementDealerWins();
        stats.incrementDraws();

        assertEquals(1, stats.getPlayerWins());
        assertEquals(1, stats.getDealerWins());
        assertEquals(1, stats.getDraws());
        assertFalse(stats.isEmpty());
        assertTrue(stats.getTotalGames() > 0);
    }
}
