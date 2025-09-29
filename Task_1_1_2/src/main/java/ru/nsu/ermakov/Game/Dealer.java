package ru.nsu.ermakov.Game;

import ru.nsu.ermakov.Kartishki.Deck;
import ru.nsu.ermakov.Enums.GameResult;
import ru.nsu.ermakov.Kartishki.InputHandler;
import ru.nsu.ermakov.Kartishki.Player;

/**
 * Класс, представляющий дилера.
 */
public class Dealer extends Player {
    /**
     * Создаёт дилера.
     *
     * @param name имя дилера
     */
    public Dealer(String name) {
        super(name);
    }

    /**
     * Раздаёт начальные карты.
     *
     * @param player игрок
     * @param deck   колода
     */
    public void dealInitialCards(Player player, Deck deck) {
        clearHand();
        player.clearHand();

        player.takeCard(deck.draw());
        this.takeCard(deck.draw());
        player.takeCard(deck.draw());
        this.takeCard(deck.draw());

        GamePrinter.printDealerInitial(this);
    }

    /**
     * Логика хода дилера.
     *
     * @param deck колода
     */
    public void playTurn(Deck deck) {
        GamePrinter.printDealerReveal(this);

        while (getScore() < 17) {
            InputHandler.Card card = deck.draw();
            takeCard(card);
            GamePrinter.printDealerDraw(card, this);
        }
    }

    /**
     * Сравнение результата дилера и игрока.
     *
     * @param player игрок
     * @return результат игры
     */
    public GameResult compareWithPlayer(Player player) {
        int dealerScore = getScore();
        int playerScore = player.getScore();

        if (isBusted()) {
            return GameResult.PLAYER_WIN;
        } else if (player.isBusted()) {
            return GameResult.DEALER_WIN;
        } else if (dealerScore > playerScore) {
            return GameResult.DEALER_WIN;
        } else if (dealerScore < playerScore) {
            return GameResult.PLAYER_WIN;
        } else {
            return GameResult.DRAW;
        }
    }
}
