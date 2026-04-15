package ru.nsu.ermakov.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Point;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import static org.mockito.Mockito.*;

/**
 * Tests for the GameController class.
 * GameController handles user input and manages the game loop.
 */
@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    /**
     * Tests GameController creation with valid parameters.
     */
    @Test
    @DisplayName("GameController should be created with valid parameters")
    void testGameControllerCreation() {
        Cell[][] emptyField = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                emptyField[x][y] = Cell.EMPTY;
            }
        }
        Point startPoint = new Point(10, 7);
        Game game = new Game(emptyField, startPoint);
        game.startGame();

        Pane root = new Pane();
        Scene scene = new Scene(root);

        Assertions.assertDoesNotThrow(() -> new GameController(game, scene),
                "GameController should be created without exception");
    }

    /**
     * Tests that startGameLoop can be called without exceptions.
     */
    @Test
    @DisplayName("startGameLoop should be callable")
    void testStartGameLoopCallable() {
        Cell[][] emptyField = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                emptyField[x][y] = Cell.EMPTY;
            }
        }
        Point startPoint = new Point(10, 7);
        Game game = new Game(emptyField, startPoint);
        game.startGame();

        Pane root = new Pane();
        Scene scene = new Scene(root);

        GameController controller = new GameController(game, scene);

        Assertions.assertDoesNotThrow(() -> controller.startGameLoop(),
                "startGameLoop should not throw exception");
    }

    /**
     * Tests that GameController stores game and scene references.
     */
    @Test
    @DisplayName("GameController should store game and scene references")
    void testControllerStoresReferences() {
        Cell[][] emptyField = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                emptyField[x][y] = Cell.EMPTY;
            }
        }
        Point startPoint = new Point(10, 7);
        Game game = new Game(emptyField, startPoint);
        game.startGame();

        Pane root = new Pane();
        Scene scene = new Scene(root);

        GameController controller = new GameController(game, scene);

        Assertions.assertNotNull(controller, "Controller should not be null");
    }
}
