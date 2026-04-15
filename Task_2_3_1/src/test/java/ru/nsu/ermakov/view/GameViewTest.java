package ru.nsu.ermakov.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.model.AppState;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.model.Point;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;

/**
 * Tests for the GameView class.
 * GameView handles rendering the game state to the canvas.
 */
class GameViewTest {

    /**
     * Tests GameView creation with valid parameters.
     */
    @Test
    @DisplayName("GameView should be created with valid parameters")
    void testGameViewCreation() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Assertions.assertDoesNotThrow(() -> new GameView(gc, 40),
                "GameView should be created without exception");
    }

    /**
     * Tests that GameView stores graphics context and cell size.
     */
    @Test
    @DisplayName("GameView should store graphics context and cell size")
    void testViewStoresParameters() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int cellSize = 40;

        GameView view = new GameView(gc, cellSize);

        Assertions.assertNotNull(view, "View should not be null");
    }

    /**
     * Tests that update method can be called without exceptions.
     */
    @Test
    @DisplayName("update method should be callable")
    void testUpdateCallable() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 20, 15, false, 0, AppState.PLAYING
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "update method should not throw exception");
    }

    /**
     * Tests that render method can be called with paused state.
     */
    @Test
    @DisplayName("render should handle paused state")
    void testRenderPausedState() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 20, 15, true, 0, AppState.PAUSED
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "render should handle paused state");
    }

    /**
     * Tests that render method can be called with game over state.
     */
    @Test
    @DisplayName("render should handle game over state")
    void testRenderGameOverState() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, true, 20, 15, false, 10, AppState.GAME_OVER
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "render should handle game over state");
    }

    /**
     * Tests that render method can handle walls in the field.
     */
    @Test
    @DisplayName("render should handle walls in field")
    void testRenderWithWalls() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }
        field[5][5] = Cell.WALL;
        field[10][10] = Cell.WALL;

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 20, 15, false, 0, AppState.PLAYING
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "render should handle walls");
    }

    /**
     * Tests that render method can handle food in the field.
     */
    @Test
    @DisplayName("render should handle food in field")
    void testRenderWithFood() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }
        field[5][5] = Cell.FOOD;
        field[10][10] = Cell.FOOD;

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 20, 15, false, 0, AppState.PLAYING
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "render should handle food");
    }

    /**
     * Tests that render method can handle longer snake.
     */
    @Test
    @DisplayName("render should handle longer snake")
    void testRenderLongerSnake() {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Cell[][] field = new Cell[20][15];
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 15; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(10, 7));
        snakeBody.add(new Point(10, 8));
        snakeBody.add(new Point(10, 9));
        snakeBody.add(new Point(10, 10));
        snakeBody.add(new Point(11, 10));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 20, 15, false, 2, AppState.PLAYING
        );

        Assertions.assertDoesNotThrow(() -> view.update(state),
                "render should handle longer snake");
    }

    /**
     * Tests getDirectionBetween method using reflection.
     */
    @Test
    @DisplayName("getDirectionBetween should return correct direction")
    void testGetDirectionBetween() throws Exception {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Method method = GameView.class.getDeclaredMethod("getDirectionBetween",
                Point.class, Point.class);
        method.setAccessible(true);

        Point from = new Point(5, 5);
        Point toRight = new Point(6, 5);
        Point toLeft = new Point(4, 5);
        Point toDown = new Point(5, 6);
        Point toUp = new Point(5, 4);

        Direction dirRight = (Direction) method.invoke(view, from, toRight);
        Direction dirLeft = (Direction) method.invoke(view, from, toLeft);
        Direction dirDown = (Direction) method.invoke(view, from, toDown);
        Direction dirUp = (Direction) method.invoke(view, from, toUp);

        Assertions.assertEquals(Direction.RIGHT, dirRight, "Should return RIGHT for x+");
        Assertions.assertEquals(Direction.LEFT, dirLeft, "Should return LEFT for x-");
        Assertions.assertEquals(Direction.DOWN, dirDown, "Should return DOWN for y+");
        Assertions.assertEquals(Direction.UP, dirUp, "Should return UP for y-");
    }

    /**
     * Tests getCornerDirection method using reflection.
     */
    @Test
    @DisplayName("getCornerDirection should return correct corner direction")
    void testGetCornerDirection() throws Exception {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameView view = new GameView(gc, 40);

        Method method = GameView.class.getDeclaredMethod("getCornerDirection",
                Direction.class, Direction.class);
        method.setAccessible(true);

        Direction corner1 = (Direction) method.invoke(view, Direction.UP, Direction.RIGHT);
        Direction corner2 = (Direction) method.invoke(view, Direction.RIGHT, Direction.DOWN);
        Direction corner3 = (Direction) method.invoke(view, Direction.DOWN, Direction.LEFT);
        Direction corner4 = (Direction) method.invoke(view, Direction.LEFT, Direction.UP);

        Assertions.assertEquals(Direction.RIGHT, corner1, "UP+RIGHT should return RIGHT");
        Assertions.assertEquals(Direction.DOWN, corner2, "RIGHT+DOWN should return DOWN");
        Assertions.assertEquals(Direction.LEFT, corner3, "DOWN+LEFT should return LEFT");
        Assertions.assertEquals(Direction.UP, corner4, "LEFT+UP should return UP");
    }
}
