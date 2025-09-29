package ru.nsu.ermakov.Game;

import ru.nsu.ermakov.Kartishki.InputHandler;

import java.util.Scanner;

/**
 * Основной игровой процесс: несколько раундов,
 * ведение счёта, взаимодействие с игроком.
 */
public class Game {
    private final Scanner scanner = new Scanner(System.in);
    private final GameStats stats = new GameStats();

    /**
     * Запускает цикл игры.
     */
    public void start() {
        GamePrinter.printWelcome();
        int roundNumber = 1;
        while (true) {
            Round round = new Round(roundNumber, stats, scanner);
            round.play();
            if (!InputHandler.askYesNo(scanner, "Играть ещё раунд? (y/n): ")) {
                break;
            }
            roundNumber++;
        }
        GamePrinter.printGoodbye(stats);
    }
}
