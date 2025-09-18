package ru.nsu.ermakov;

import java.util.Scanner;

/**
 * Консольная игра Блэкджек.
 */
public class Blackjack {
    private final Deck deck = new Deck(1);
    private final Player player = new Player("Игрок");
    private final Player dealer = new Player("Дилер");
    private int scorePlayer = 0;
    private int scoreDealer = 0;
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("Добро пожаловать в Блэкджек!");
        int round = 1;
        while (true) {
            System.out.println("\nРаунд " + round);
            playRound();
            System.out.printf("Счёт %d:%d (Вы : Дилер)%n", scorePlayer, scoreDealer);
            System.out.print("Играть ещё раунд? (y/n): ");
            if (!scanner.next().equalsIgnoreCase("y")) {
                break;
            }
            round++;
        }
    }

    private void playRound() {
        player.clearHand();
        dealer.clearHand();

        // Раздача
        player.addCard(deck.draw());
        dealer.addCard(deck.draw());
        player.addCard(deck.draw());
        dealer.addCard(deck.draw());

        System.out.println("Дилер раздал карты");
        System.out.println("Ваши карты: " + player.showHand(false));
        System.out.println("Карты дилера: " + dealer.showHand(true));

        // Проверка на блэкджек
        if (player.getScore() == 21) {
            System.out.println("У вас блэкджек! 🎉");
            scorePlayer++;
            return;
        }

        // Ход игрока
        while (true) {
            System.out.println("\nВаш ход");
            System.out.print("Введите '1', чтобы взять карту, и '0', чтобы остановиться: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                Card card = deck.draw();
                player.addCard(card);
                System.out.println("Вы открыли карту " + card);
                System.out.println("Ваши карты: " + player.showHand(false));
                if (player.getScore() > 21) {
                    System.out.println("Перебор! Вы проиграли раунд.");
                    scoreDealer++;
                    return;
                }
            } else {
                break;
            }
        }

        // Ход дилера
        System.out.println("\nХод дилера");
        System.out.println("Дилер открывает закрытую карту: " + dealer.getHand().get(1));
        System.out.println("Карты дилера: " + dealer.showHand(false));

        while (dealer.getScore() < 17) {
            Card card = deck.draw();
            dealer.addCard(card);
            System.out.println("Дилер открывает карту " + card);
            System.out.println("Карты дилера: " + dealer.showHand(false));
        }

        // Определение победителя
        int p = player.getScore();
        int d = dealer.getScore();

        if (d > 21 || p > d) {
            System.out.println("Вы выиграли раунд!");
            scorePlayer++;
        } else if (p == d) {
            System.out.println("Ничья!");
        } else {
            System.out.println("Дилер выиграл раунд!");
            scoreDealer++;
        }
    }

    public static void main(String[] args) {
        new Blackjack().start();
    }
}
