package ru.nsu.ermakov.Kartishki;

import ru.nsu.ermakov.Enums.Command;
import ru.nsu.ermakov.Enums.Rank;
import ru.nsu.ermakov.Enums.Suit;

import java.util.Scanner;

/**
 * Утилита для работы с вводом игрока.
 */
public class InputHandler {
    /**
     * Спрашивает у игрока, брать ли карту.
     *
     * @param scanner сканер
     * @return команда
     */
    public static Command askPlayerMove(Scanner scanner) {
        while (true) {
            System.out.print("Введите '1', чтобы взять карту, или '0', чтобы остановиться: ");
            String input = scanner.next().trim();

            if (input.equals("1")) {
                return Command.HIT;
            } else if (input.equals("0")) {
                return Command.STAND;
            } else {
                System.out.println("Некорректный ввод! Попробуйте снова.");
            }
        }
    }

    /**
     * Спрашивает у игрока "да/нет".
     *
     * @param scanner сканер
     * @param message сообщение
     * @return true, если ответ "y"
     */
    public static boolean askYesNo(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.next().equalsIgnoreCase("y");
    }

    /**
     * Класс, представляющий карту в игре Блэкджек.
     */
    public static class Card {
        private final Rank rank;
        private final Suit suit;

        /**
         * Конструктор карты.
         *
         * @param rank номинал
         * @param suit масть
         */
        public Card(Rank rank, Suit suit) {
            this.rank = rank;
            this.suit = suit;
        }

        /**
         * Возвращает номинал карты.
         */
        public Rank getRank() {
            return rank;
        }

        /**
         * Возвращает масть карты.
         */
        public Suit getSuit() {
            return suit;
        }

        /**
         * Возвращает базовое значение карты.
         */
        public int getBaseValue() {
            return rank.getValue();
        }

        @Override
        public String toString() {
            return rank.getName() + " " + suit.getName();
        }
    }
}
