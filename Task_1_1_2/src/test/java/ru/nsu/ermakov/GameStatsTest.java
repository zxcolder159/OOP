package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameStatsTest {
    @Test
    void testAddWins() {
        GameStats stats = new GameStats();
        stats.addPlayerWin();
        stats.addDealerWin();
        assertEquals(1, stats.getPlayerWins());
        assertEquals(1, stats.getDealerWins());
    }

    @Test
    void testUpdate() {
        GameStats stats = new GameStats();
        stats.update(GameResult.PLAYER_WIN);
        assertEquals(1, stats.getPlayerWins());
    }
}
