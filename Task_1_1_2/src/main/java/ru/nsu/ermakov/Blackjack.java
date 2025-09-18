package ru.nsu.ermakov;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Консольная реализация игры Блэкджек.
 */
public class Blackjack {
    private Deck deck;
    private final Player player = new Player("Игрок");
    private final Player dealer = new Player("Дилер");
    private int scorePlayer = 0;
    private int scoreDealer = 0;
    private final Scanner scanner;
    private final PrintStream out;

    /**
     * Создает игру, используя стандартные потоки ввода и вывода.
     */
    public Blackjack() {
        this(System.in, System.out);
    }

    /**
     * Создает игру с заданными потоками ввода и вывода.
     *
     * @param in  входной поток
     * @param out выходной поток
     */
    public Blackjack(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
        this.deck = new Deck(1);
    }

    /**
     * Создает игру с заданными потоками и колодой (для тестов).
     *
     * @param in   входной поток
     * @param out  выходной поток
     * @param deck кастомная колода
     */
    public Blackjack(InputStream in, PrintStream out, Deck deck) {
        this.scanner = new Scanner(in);
        this.out = out;
        this.deck = deck;
    }

    /**
     * Запускает игру, состоящую из последовательности раундов.
     */
    public void start() {
        out.println("Добро пожаловать в Блэкджек!");
        int round = 1;
        while (true) {
            out.println("\nРаунд " + round);
            playRound();
            out.printf("Счёт %d:%d (Вы : Дилер)%n", scorePlayer, scoreDealer);
            out.print("Играть ещё раунд? (y/n): ");
            if (!scanner.next().equalsIgnoreCase("y")) {
                break;
            }
            round++;
        }
    }

    /**
     * Проводит один раунд игры.
     */
    public void playRound() {
        player.clearHand();
        dealer.clearHand();

        player.addCard(deck.draw());
        dealer.addCard(deck.draw());
        player.addCard(deck.draw());
        dealer.addCard(deck.draw());

        out.println("Дилер раздал карты");
        out.println("Ваши карты: " + player.showHand(false));
        out.println("Карты дилера: " + dealer.showHand(true));

        if (player.getScore() == 21) {
            out.println("У вас блэкджек! 🎉");
            scorePlayer++;
            return;
        }

        while (true) {
            out.println("\nВаш ход");
            out.print("Введите '1', чтобы взять карту, и '0', чтобы остановиться: ");
            int choice = scanner.hasNextInt() ? scanner.nextInt() : 0;
            if (choice == 1) {
                Card card = deck.draw();
                player.addCard(card);
                out.println("Вы открыли карту " + card);
                out.println("Ваши карты: " + player.showHand(false));
                if (player.getScore() > 21) {
                    out.println("Перебор! Вы проиграли раунд.");
                    scoreDealer++;
                    return;
                }
            } else {
                break;
            }
        }

        out.println("\nХод дилера");
        out.println("Дилер открывает закрытую карту: " + dealer.getHand().get(1));
        out.println("Карты дилера: " + dealer.showHand(false));

        while (dealer.getScore() < 17) {
            Card card = deck.draw();
            dealer.addCard(card);
            out.println("Дилер открывает карту " + card);
            out.println("Карты дилера: " + dealer.showHand(false));
        }

        int p = player.getScore();
        int d = dealer.getScore();

        if (d > 21 || p > d) {
            out.println("Вы выиграли раунд!");
            scorePlayer++;
        } else if (p == d) {
            out.println("Ничья!");
        } else {
            out.println("Дилер выиграл раунд!");
            scoreDealer++;
        }
    }

    /**
     * Возвращает общий счёт игрока по раундам.
     *
     * @return счёт игрока
     */
    public int getPlayerScoreTotal() {
        return scorePlayer;
    }

    /**
     * Возвращает общий счёт дилера по раундам.
     *
     * @return счёт дилера
     */
    public int getDealerScoreTotal() {
        return scoreDealer;
    }

    /**
     * Возвращает игрока.
     *
     * @return объект игрока
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Возвращает дилера.
     *
     * @return объект дилера
     */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Точка входа в игру.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        new Blackjack().start();
    }
}
