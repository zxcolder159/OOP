package ru.nsu.ermakov;

/**
 * Хранение статистики игры.
 */
public class GameStats {
    private int playerWins = 0;
    private int dealerWins = 0;

    /**
     * Добавляет победу игрока.
     */
    public void addPlayerWin() {
        playerWins++;
    }

    /**
     * Добавляет победу дилера.
     */
    public void addDealerWin() {
        dealerWins++;
    }

    /**
     * Обновляет статистику по результату раунда.
     */
    public void update(GameResult result) {
        switch (result) {
            case PLAYER_WIN -> addPlayerWin();
            case DEALER_WIN -> addDealerWin();
            default -> {
            }
        }
    }

    public int getPlayerWins() {
        return playerWins;
    }

    public int getDealerWins() {
        return dealerWins;
    }
}
