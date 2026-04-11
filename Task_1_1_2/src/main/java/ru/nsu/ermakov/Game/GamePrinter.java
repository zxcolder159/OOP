package ru.nsu.ermakov.Game;

import ru.nsu.ermakov.Enums.GameResult;
import ru.nsu.ermakov.Kartishki.InputHandler;
import ru.nsu.ermakov.Kartishki.Player;

/**
 * Утилита для вывода сообщений на экран.
 */
public class GamePrinter {
    public static void printWelcome() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    public static void printGoodbye(GameStats stats) {
        System.out.printf("Игра окончена. Счёт %d:%d (Вы : Дилер)%n",
                stats.getPlayerWins(), stats.getDealerWins());
    }

    public static void printRoundStart(int round) {
        System.out.println("\nРаунд " + round);
    }

    public static void printPlayerHand(Player player, boolean hideFirst) {
        System.out.println("Ваши карты: " + player.showHand(hideFirst));
    }

    public static void printDealerInitial(Dealer dealer) {
        System.out.println("Дилер раздал карты.");
        System.out.println("Карты дилера: " + dealer.showHand(true));
    }

    public static void printDealerReveal(Dealer dealer) {
        System.out.println("\nХод дилера.");
        System.out.println("Карты дилера: " + dealer.showHand(false));
    }

    public static void printDealerDraw(InputHandler.Card card, Dealer dealer) {
        System.out.println("Дилер открыл карту: " + card);
        System.out.println("Карты дилера: " + dealer.showHand(false));
    }

    public static void printResult(GameResult result) {
        switch (result) {
            case PLAYER_WIN -> System.out.println("Вы выиграли раунд!");
            case DEALER_WIN -> System.out.println("Дилер выиграл раунд!");
            case DRAW -> System.out.println("Ничья!");
        }
    }

    public static void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли раунд.");
    }
}
