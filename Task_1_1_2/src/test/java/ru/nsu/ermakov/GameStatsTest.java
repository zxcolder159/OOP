package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStatsTest {
    @Test
    void testInitialValues() {
        GameStats stats = new GameStats();
        assertEquals(0, stats.getPlayerWins());
        assertEquals(0, stats.getDealerWins());
    }

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
        stats.update(GameResult.DEALER_WIN);
        stats.update(GameResult.DRAW); 
        assertEquals(1, stats.getPlayerWins());
        assertEquals(1, stats.getDealerWins());
    }
}
