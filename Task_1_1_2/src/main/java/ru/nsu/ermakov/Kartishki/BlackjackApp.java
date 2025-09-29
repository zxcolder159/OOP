package ru.nsu.ermakov.Kartishki;

import ru.nsu.ermakov.Game.Game;

/**
 * Точка входа в консольное приложение "Блэкджек".
 */
public class BlackjackApp {
    /**
     * Запуск приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
