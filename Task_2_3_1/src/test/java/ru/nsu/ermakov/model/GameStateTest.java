package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the GameState class.
 * GameState is an immutable data class that represents the current state of the game.
 */
class GameStateTest {

    /**
     * Tests GameState creation with all parameters.
     */
    @Test
    @DisplayName("GameState should store all parameters correctly")
    void testGameStateCreation() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));
        snakeBody.add(new Point(4, 5));
        snakeBody.add(new Point(3, 5));

        Cell[][] field = new Cell[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
        field[7][7] = Cell.FOOD;

        GameState state = new GameState(
                snakeBody, Direction.RIGHT, field, false, 10, 10, false, 42, AppState.PLAYING
        );

        Assertions.assertEquals(snakeBody, state.getSnakeBody(), "Snake body should match");
        Assertions.assertEquals(Direction.RIGHT, state.getSnakeDirection(), "Direction should match");
        Assertions.assertEquals(10, state.getWidth(), "Width should match");
        Assertions.assertEquals(10, state.getHeight(), "Height should match");
        Assertions.assertFalse(state.isGameOver(), "Game should not be over");
        Assertions.assertFalse(state.isPaused(), "Game should not be paused");
        Assertions.assertEquals(42, state.getScore(), "Score should match");
    }

    /**
     * Tests GameState with game over flag set.
     */
    @Test
    @DisplayName("GameState should handle game over state")
    void testGameOverState() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        Cell[][] field = new Cell[10][10];

        GameState state = new GameState(
                snakeBody, Direction.UP, field, true, 10, 10, false, 0, AppState.GAME_OVER
        );

        Assertions.assertTrue(state.isGameOver(), "Game should be over");
        Assertions.assertFalse(state.isPaused(), "Game should not be paused when over");
    }

    /**
     * Tests GameState with paused flag set.
     */
    @Test
    @DisplayName("GameState should handle paused state")
    void testPausedState() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        Cell[][] field = new Cell[10][10];

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, true, 10, AppState.PAUSED
        );

        Assertions.assertTrue(state.isPaused(), "Game should be paused");
        Assertions.assertFalse(state.isGameOver(), "Game should not be over when paused");
        Assertions.assertEquals(10, state.getScore(), "Score should be preserved when paused");
    }

    /**
     * Tests GameState with field data.
     */
    @Test
    @DisplayName("GameState should store field correctly")
    void testFieldStorage() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        Cell[][] field = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
        field[2][2] = Cell.FOOD;
        field[3][3] = Cell.WALL;

        GameState state = new GameState(
                snakeBody, Direction.LEFT, field, false, 5, 5, false, 0, AppState.PLAYING
        );

        Cell[][] storedField = state.getField();
        Assertions.assertEquals(Cell.FOOD, storedField[2][2], "Food should be at (2,2)");
        Assertions.assertEquals(Cell.WALL, storedField[3][3], "Wall should be at (3,3)");
        Assertions.assertEquals(5, storedField.length, "Field width should match");
        Assertions.assertEquals(5, storedField[0].length, "Field height should match");
    }

    /**
     * Tests GameState with different scores.
     */
    @Test
    @DisplayName("GameState should handle various scores")
    void testVariousScores() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));
        Cell[][] field = new Cell[10][10];

        GameState stateZero = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0, AppState.PLAYING
        );
        Assertions.assertEquals(0, stateZero.getScore(), "Score should be 0");

        GameState stateHigh = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 999, AppState.PLAYING
        );
        Assertions.assertEquals(999, stateHigh.getScore(), "Score should be 999");
    }

    /**
     * Tests GameState with different directions.
     */
    @Test
    @DisplayName("GameState should handle all directions")
    void testAllDirections() {
        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));
        Cell[][] field = new Cell[10][10];

        GameState stateUp = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0, AppState.PLAYING
        );
        Assertions.assertEquals(Direction.UP, stateUp.getSnakeDirection(), "Direction should be UP");

        GameState stateDown = new GameState(
                snakeBody, Direction.DOWN, field, false, 10, 10, false, 0, AppState.PLAYING
        );
        Assertions.assertEquals(Direction.DOWN, stateDown.getSnakeDirection(), "Direction should be DOWN");

        GameState stateLeft = new GameState(
                snakeBody, Direction.LEFT, field, false, 10, 10, false, 0, AppState.PLAYING
        );
        Assertions.assertEquals(Direction.LEFT, stateLeft.getSnakeDirection(), "Direction should be LEFT");

        GameState stateRight = new GameState(
                snakeBody, Direction.RIGHT, field, false, 10, 10, false, 0, AppState.PLAYING
        );
        Assertions.assertEquals(Direction.RIGHT, stateRight.getSnakeDirection(), "Direction should be RIGHT");
    }
}
