package ru.nsu.ermakov;

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
        System.out.print("Введите '1', чтобы взять карту, или '0', чтобы остановиться: ");
        int choice = scanner.nextInt();
        return choice == 1 ? Command.HIT : Command.STAND;
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
}
