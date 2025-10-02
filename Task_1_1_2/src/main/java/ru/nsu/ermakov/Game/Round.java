package ru.nsu.ermakov.Game;

import ru.nsu.ermakov.Enums.Command;
import ru.nsu.ermakov.Enums.GameResult;
import ru.nsu.ermakov.Kartishki.Deck;
import ru.nsu.ermakov.Kartishki.InputHandler;
import ru.nsu.ermakov.Kartishki.Player;

import java.util.Scanner;

/**
 * Класс, представляющий один раунд игры.
 */
public class Round {
    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final int roundNumber;
    private final GameStats stats;
    private final Scanner scanner;

    /**
     * Создаёт новый раунд.
     *
     * @param roundNumber номер раунда
     * @param stats       статистика игры
     * @param scanner     сканер для ввода
     */
    public Round(int roundNumber, GameStats stats, Scanner scanner) {
        this.roundNumber = roundNumber;
        this.stats = stats;
        this.scanner = scanner;
        this.deck = new Deck(1);
        this.player = new Player("Игрок");
        this.dealer = new Dealer("Дилер");
    }

    /**
     * Запускает игру в этом раунде.
     */
    public void play() {
        GamePrinter.printRoundStart(roundNumber);
        dealer.dealInitialCards(player, deck);
        GamePrinter.printPlayerHand(player, false);
        // Ход игрока
        while (true) {
            Command command = InputHandler.askPlayerMove(scanner);
            if (command == Command.HIT) {
                player.takeCard(deck.draw());
                GamePrinter.printPlayerHand(player, false);
                if (player.isBusted()) {
                    GamePrinter.printPlayerBust();
                    stats.addDealerWin();
                    return;
                }
            } else {
                break;
            }
        }

        // Ход дилера
        dealer.playTurn(deck);

        // Определение результата
        GameResult result = dealer.compareWithPlayer(player);
        stats.update(result);
        GamePrinter.printResult(result);
    }
}
