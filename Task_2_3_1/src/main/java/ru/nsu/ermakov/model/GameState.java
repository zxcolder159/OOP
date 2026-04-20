package ru.nsu.ermakov.model;

import java.util.List;

/**
 * Класс, хранящий состояние игры.
 */
public class GameState {
    private final List<Point> snakeBody;
    private final Direction snakeDirection;
    private final Cell[][] field;
    private final boolean gameOver;
    private final int width;
    private final int height;
    private final boolean paused;
    private final int score;
    private final AppState appState;

    /**
     * Конструктор состояния игры.
     *
     * @param snakeBody тело змейки
     * @param snakeDirection направление змейки
     * @param field игровое поле
     * @param gameOver флаг окончания игры
     * @param width ширина поля
     * @param height высота поля
     * @param paused флаг паузы
     * @param score счет
     * @param appState состояние приложения
     */
    public GameState(List<Point> snakeBody, Direction snakeDirection, Cell[][] field,
                     boolean gameOver, int width, int height, boolean paused,
                     int score, AppState appState) {
        this.snakeBody = snakeBody;
        this.snakeDirection = snakeDirection;
        this.field = field;
        this.gameOver = gameOver;
        this.width = width;
        this.height = height;
        this.paused = paused;
        this.score = score;
        this.appState = appState;
    }

    /**
     * Возвращает тело змейки.
     *
     * @return тело змейки
     */
    public List<Point> getSnakeBody() {
        return snakeBody;
    }

    /**
     * Возвращает направление змейки.
     *
     * @return направление змейки
     */
    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    /**
     * Возвращает игровое поле.
     *
     * @return игровое поле
     */
    public Cell[][] getField() {
        return field;
    }

    /**
     * Проверяет, окончена ли игра.
     *
     * @return true если игра окончена
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Возвращает ширину поля.
     *
     * @return ширина поля
     */
    public int getWidth() {
        return width;
    }

    /**
     * Возвращает высоту поля.
     *
     * @return высота поля
     */
    public int getHeight() {
        return height;
    }

    /**
     * Проверяет, находится ли игра на паузе.
     *
     * @return true если игра на паузе
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Возвращает счет.
     *
     * @return счет
     */
    public int getScore() {
        return score;
    }

    /**
     * Возвращает состояние приложения.
     *
     * @return состояние приложения
     */
    public AppState getAppState() {
        return appState;
    }
}
