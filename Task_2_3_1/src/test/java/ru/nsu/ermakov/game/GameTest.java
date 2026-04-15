package ru.nsu.ermakov.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.GameObserver;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.model.Point;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests for the Game class.
 * Game manages the game state, snake movement, scoring, and observers.
 */
class GameTest {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    private Cell[][] emptyField;

    /**
     * Sets up an empty field before each test.
     */
    @BeforeEach
    void setUp() {
        emptyField = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                emptyField[x][y] = Cell.EMPTY;
            }
        }
    }

    private Game createStartedGame(Point startPoint) {
        Game game = new Game(emptyField, startPoint);
        game.startGame();
        return game;
    }

    private Game createStartedGame(Cell[][] field, Point startPoint) {
        Game game = new Game(field, startPoint);
        game.startGame();
        return game;
    }

    /**
     * Tests game creation with valid parameters.
     */
    @Test
    @DisplayName("Game should be created with valid parameters")
    void testGameCreation() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        Assertions.assertNotNull(game, "Game should be created");
        Assertions.assertFalse(game.isGameOver(), "Game should not be over initially");
        Assertions.assertFalse(game.isPaused(), "Game should not be paused initially");
        Assertions.assertEquals(0, game.getScore(), "Score should be 0 initially");
    }

    /**
     * Tests that food is spawned on game creation.
     */
    @Test
    @DisplayName("Game should spawn food on creation")
    void testFoodSpawned() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        GameState state = game.getState();
        boolean hasFood = false;
        Cell[][] field = state.getField();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (field[x][y] == Cell.FOOD) {
                    hasFood = true;
                    break;
                }
            }
        }
        Assertions.assertTrue(hasFood, "Food should be spawned on game creation");
    }

    /**
     * Tests setting move interval.
     */
    @Test
    @DisplayName("Game should allow setting move interval")
    void testSetMoveInterval() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        game.setMoveIntervalNanos(50_000_000L);
        Assertions.assertEquals(50_000_000L, game.getMoveIntervalNanos(),
                "Move interval should be updated");
    }

    /**
     * Tests that negative move interval is rejected.
     */
    @Test
    @DisplayName("Game should reject negative move interval")
    void testRejectNegativeMoveInterval() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        long originalInterval = game.getMoveIntervalNanos();
        game.setMoveIntervalNanos(-100);
        Assertions.assertEquals(originalInterval, game.getMoveIntervalNanos(),
                "Negative interval should be rejected");
    }

    /**
     * Tests increasing speed.
     */
    @Test
    @DisplayName("Game should increase speed")
    void testIncreaseSpeed() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        long originalInterval = game.getMoveIntervalNanos();
        game.increaseSpeed(10_000_000L);
        Assertions.assertEquals(originalInterval - 10_000_000L, game.getMoveIntervalNanos(),
                "Speed should increase (interval decrease)");
    }

    /**
     * Tests speed doesn't go below minimum.
     */
    @Test
    @DisplayName("Game speed should not go below minimum")
    void testSpeedMinimum() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        game.setMoveIntervalNanos(55_000_000L);
        game.increaseSpeed(10_000_000L);
        Assertions.assertEquals(50_000_000L, game.getMoveIntervalNanos(),
                "Speed should not go below 50 million nanoseconds");
    }

    /**
     * Tests changing snake direction.
     */
    @Test
    @DisplayName("Game should allow changing snake direction")
    void testChangeSnakeDirection() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        boolean result = game.changeSnakeDirection(Direction.LEFT);
        Assertions.assertTrue(result, "Should be able to change direction to LEFT");

        result = game.changeSnakeDirection(Direction.DOWN);
        Assertions.assertTrue(result, "Should be able to change direction to DOWN");
    }

    /**
     * Tests rejecting invalid direction change.
     */
    @Test
    @DisplayName("Game should reject invalid direction change")
    void testRejectInvalidDirection() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        boolean result = game.changeSnakeDirection(Direction.DOWN);
        Assertions.assertFalse(result, "Should not be able to change from UP to DOWN");
    }

    /**
     * Tests game step advances snake.
     */
    @Test
    @DisplayName("Game step should advance snake")
    void testGameStep() {
        Point startPoint = new Point(10, 10);
        Game game = createStartedGame(startPoint);
        game.changeSnakeDirection(Direction.RIGHT);

        GameState initialState = game.getState();
        Point initialHead = initialState.getSnakeBody().get(0);

        game.step();

        GameState newState = game.getState();
        Point newHead = newState.getSnakeBody().get(0);

        Assertions.assertEquals(initialHead.x() + 1, newHead.x(), "Snake should move right");
        Assertions.assertEquals(initialHead.y(), newHead.y(), "Y coordinate should stay same");
    }

    /**
     * Tests game state returns correct values.
     */
    @Test
    @DisplayName("Game state should contain correct values")
    void testGameState() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        GameState state = game.getState();

        Assertions.assertEquals(WIDTH, state.getWidth(), "Width should match");
        Assertions.assertEquals(HEIGHT, state.getHeight(), "Height should match");
        Assertions.assertFalse(state.isGameOver(), "Game should not be over");
        Assertions.assertFalse(state.isPaused(), "Game should not be paused");
        Assertions.assertEquals(0, state.getScore(), "Score should be 0");
        Assertions.assertNotNull(state.getField(), "Field should not be null");
        Assertions.assertNotNull(state.getSnakeBody(), "Snake body should not be null");
    }

    /**
     * Tests toggle pause functionality.
     */
    @Test
    @DisplayName("Game should toggle pause state")
    void testTogglePause() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        Assertions.assertFalse(game.isPaused(), "Game should not be paused initially");

        game.togglePause();
        Assertions.assertTrue(game.isPaused(), "Game should be paused after toggle");

        game.togglePause();
        Assertions.assertFalse(game.isPaused(), "Game should not be paused after second toggle");
    }

    /**
     * Tests that step does nothing when paused.
     */
    @Test
    @DisplayName("Game step should do nothing when paused")
    void testStepWhenPaused() {
        Point startPoint = new Point(10, 10);
        Game game = createStartedGame(startPoint);
        game.changeSnakeDirection(Direction.RIGHT);

        GameState initialState = game.getState();
        Point initialHead = initialState.getSnakeBody().get(0);

        game.togglePause();
        game.step();

        GameState newState = game.getState();
        Point newHead = newState.getSnakeBody().get(0);

        Assertions.assertEquals(initialHead, newHead, "Snake should not move when paused");
    }

    /**
     * Tests that step does nothing when game over.
     */
    @Test
    @DisplayName("Game step should do nothing when game over")
    void testStepWhenGameOver() {
        Cell[][] fieldWithWall = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                fieldWithWall[x][y] = Cell.EMPTY;
            }
        }
        fieldWithWall[10][9] = Cell.WALL;

        Point startPoint = new Point(10, 10);
        Game game = createStartedGame(fieldWithWall, startPoint);

        game.step();

        Assertions.assertTrue(game.isGameOver(), "Game should be over");

        GameState stateBefore = game.getState();
        game.step();
        GameState stateAfter = game.getState();

        Assertions.assertEquals(stateBefore.getSnakeBody(), stateAfter.getSnakeBody(),
                "Snake should not move when game over");
    }

    /**
     * Tests that toggle pause works normally.
     */
    @Test
    @DisplayName("Game togglePause should work when game is running")
    void testTogglePauseWhenRunning() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        game.togglePause();
        Assertions.assertTrue(game.isPaused(), "Game should be paused");

        game.togglePause();
        Assertions.assertFalse(game.isPaused(), "Game should be unpaused");
    }

    /**
     * Tests that game ends when snake hits a wall.
     */
    @Test
    @DisplayName("Game should end when snake hits a wall")
    void testGameOverOnWallHit() {
        Cell[][] fieldWithWall = new Cell[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                fieldWithWall[x][y] = Cell.EMPTY;
            }
        }
        fieldWithWall[10][9] = Cell.WALL;

        Point startPoint = new Point(10, 10);
        Game game = createStartedGame(fieldWithWall, startPoint);

        game.step();

        Assertions.assertTrue(game.isGameOver(), "Game should be over after hitting wall");
    }

    /**
     * Tests adding observer.
     */
    @Test
    @DisplayName("Game should allow adding observers")
    void testAddObserver() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        AtomicInteger callCount = new AtomicInteger(0);
        GameObserver observer = state -> callCount.incrementAndGet();

        game.addObserver(observer);
        game.step();

        Assertions.assertTrue(callCount.get() > 0, "Observer should be called");
    }

    /**
     * Tests removing observer.
     */
    @Test
    @DisplayName("Game should allow removing observers")
    void testRemoveObserver() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        AtomicInteger callCount = new AtomicInteger(0);
        GameObserver observer = state -> callCount.incrementAndGet();

        game.addObserver(observer);
        game.step();
        int callsAfterFirstStep = callCount.get();

        game.removeObserver(observer);
        game.step();
        int callsAfterSecondStep = callCount.get();

        Assertions.assertEquals(callsAfterFirstStep, callsAfterSecondStep,
                "Observer should not be called after removal");
    }

    /**
     * Tests multiple observers.
     */
    @Test
    @DisplayName("Game should notify multiple observers")
    void testMultipleObservers() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        AtomicInteger callCount1 = new AtomicInteger(0);
        AtomicInteger callCount2 = new AtomicInteger(0);

        GameObserver observer1 = state -> callCount1.incrementAndGet();
        GameObserver observer2 = state -> callCount2.incrementAndGet();

        game.addObserver(observer1);
        game.addObserver(observer2);
        game.step();

        Assertions.assertTrue(callCount1.get() > 0, "First observer should be called");
        Assertions.assertTrue(callCount2.get() > 0, "Second observer should be called");
    }

    /**
     * Tests observer receives correct state.
     */
    @Test
    @DisplayName("Observer should receive correct game state")
    void testObserverReceivesState() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        GameState[] receivedState = new GameState[1];
        GameObserver observer = state -> receivedState[0] = state;

        game.addObserver(observer);
        game.step();

        Assertions.assertNotNull(receivedState[0], "Observer should receive state");
        Assertions.assertEquals(WIDTH, receivedState[0].getWidth(),
                "State should have correct width");
        Assertions.assertEquals(HEIGHT, receivedState[0].getHeight(),
                "State should have correct height");
    }

    /**
     * Tests that notify is called on toggle pause.
     */
    @Test
    @DisplayName("Observer should be notified on pause toggle")
    void testNotifyOnPause() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        AtomicInteger callCount = new AtomicInteger(0);
        GameObserver observer = state -> callCount.incrementAndGet();

        game.addObserver(observer);
        int callsBefore = callCount.get();

        game.togglePause();
        int callsAfter = callCount.get();

        Assertions.assertTrue(callsAfter > callsBefore,
                "Observer should be notified on pause toggle");
    }

    /**
     * Tests that game state is isolated from internal state.
     */
    @Test
    @DisplayName("Game state should be isolated from internal state")
    void testStateIsolation() {
        Point startPoint = new Point(10, 7);
        Game game = createStartedGame(startPoint);

        GameState state1 = game.getState();
        game.step();
        GameState state2 = game.getState();

        Assertions.assertNotSame(state1.getSnakeBody(), state2.getSnakeBody(),
                "Snake body should be different instances");
        Assertions.assertNotSame(state1.getField(), state2.getField(),
                "Field should be different instances");
    }

    /**
     * Tests direction change tracking in game state.
     */
    @Test
    @DisplayName("Game state should track last move direction")
    void testLastMoveDirectionTracking() {
        Point startPoint = new Point(10, 10);
        Game game = createStartedGame(startPoint);
        game.changeSnakeDirection(Direction.RIGHT);

        game.step();
        GameState state = game.getState();

        Assertions.assertEquals(Direction.RIGHT, state.getSnakeDirection(),
                "State should track last move direction");
    }
}
