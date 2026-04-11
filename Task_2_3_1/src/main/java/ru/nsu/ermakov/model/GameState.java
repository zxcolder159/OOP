package ru.nsu.ermakov.model;

import java.util.List;

public class GameState {
    private final List<Point> snakeBody;
    private final Direction snakeDirection;
    private final Cell[][] field;
    private final boolean gameOver;
    private final int width;
    private final int height;
    private final boolean paused;
    private final int score;

    public GameState(List<Point> snakeBody, Direction snakeDirection, Cell[][] field,
                     boolean gameOver, int width, int height, boolean paused, int score) {
        this.snakeBody = snakeBody;
        this.snakeDirection = snakeDirection;
        this.field = field;
        this.gameOver = gameOver;
        this.width = width;
        this.height = height;
        this.paused = paused;
        this.score = score;
    }

    public List<Point> getSnakeBody() {
        return snakeBody;
    }

    public Direction getSnakeDirection() {
        return snakeDirection;
    }

    public Cell[][] getField() {
        return field;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getScore() {
        return score;
    }
}
