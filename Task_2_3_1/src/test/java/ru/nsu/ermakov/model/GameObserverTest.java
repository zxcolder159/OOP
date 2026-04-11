package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the GameObserver interface.
 * GameObserver is an interface for objects that receive game state updates.
 */
class GameObserverTest {

    /**
     * Tests that a GameObserver implementation can be created.
     */
    @Test
    @DisplayName("GameObserver implementation should be created")
    void testObserverCreation() {
        List<GameState> receivedStates = new ArrayList<>();

        GameObserver observer = new GameObserver() {
            @Override
            public void update(GameState state) {
                receivedStates.add(state);
            }
        };

        Assertions.assertNotNull(observer, "Observer should be created");
    }

    /**
     * Tests that GameObserver update method receives state.
     */
    @Test
    @DisplayName("GameObserver update should receive GameState")
    void testObserverReceivesState() {
        List<GameState> receivedStates = new ArrayList<>();

        GameObserver observer = state -> receivedStates.add(state);

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0
        );

        observer.update(state);

        Assertions.assertEquals(1, receivedStates.size(), "Observer should receive one state");
        Assertions.assertEquals(state, receivedStates.get(0), "Received state should match");
    }

    /**
     * Tests that multiple states can be received.
     */
    @Test
    @DisplayName("GameObserver should receive multiple states")
    void testObserverReceivesMultipleStates() {
        List<GameState> receivedStates = new ArrayList<>();

        GameObserver observer = state -> receivedStates.add(state);

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody1 = new ArrayList<>();
        snakeBody1.add(new Point(5, 5));
        GameState state1 = new GameState(
                snakeBody1, Direction.UP, field, false, 10, 10, false, 0
        );

        List<Point> snakeBody2 = new ArrayList<>();
        snakeBody2.add(new Point(6, 5));
        GameState state2 = new GameState(
                snakeBody2, Direction.RIGHT, field, false, 10, 10, false, 1
        );

        observer.update(state1);
        observer.update(state2);

        Assertions.assertEquals(2, receivedStates.size(), "Observer should receive two states");
        Assertions.assertEquals(state1, receivedStates.get(0), "First state should match");
        Assertions.assertEquals(state2, receivedStates.get(1), "Second state should match");
    }

    /**
     * Tests GameObserver with game over state.
     */
    @Test
    @DisplayName("GameObserver should receive game over state")
    void testObserverReceivesGameOver() {
        List<Boolean> gameOverStates = new ArrayList<>();

        GameObserver observer = state -> gameOverStates.add(state.isGameOver());

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState runningState = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0
        );

        GameState gameOverState = new GameState(
                snakeBody, Direction.UP, field, true, 10, 10, false, 10
        );

        observer.update(runningState);
        observer.update(gameOverState);

        Assertions.assertFalse(gameOverStates.get(0), "First state should not be game over");
        Assertions.assertTrue(gameOverStates.get(1), "Second state should be game over");
    }

    /**
     * Tests GameObserver with paused state.
     */
    @Test
    @DisplayName("GameObserver should receive paused state")
    void testObserverReceivesPaused() {
        List<Boolean> pausedStates = new ArrayList<>();

        GameObserver observer = state -> pausedStates.add(state.isPaused());

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState runningState = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0
        );

        GameState pausedState = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, true, 0
        );

        observer.update(runningState);
        observer.update(pausedState);

        Assertions.assertFalse(pausedStates.get(0), "First state should not be paused");
        Assertions.assertTrue(pausedStates.get(1), "Second state should be paused");
    }

    /**
     * Tests that GameObserver can access all state properties.
     */
    @Test
    @DisplayName("GameObserver should access all GameState properties")
    void testObserverAccessesAllProperties() {
        int[] scores = new int[1];
        int[] widths = new int[1];
        int[] heights = new int[1];

        GameObserver observer = state -> {
            scores[0] = state.getScore();
            widths[0] = state.getWidth();
            heights[0] = state.getHeight();
        };

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 42
        );

        observer.update(state);

        Assertions.assertEquals(42, scores[0], "Observer should access score");
        Assertions.assertEquals(10, widths[0], "Observer should access width");
        Assertions.assertEquals(10, heights[0], "Observer should access height");
    }

    /**
     * Tests that GameObserver can be used in lambda form.
     */
    @Test
    @DisplayName("GameObserver should work as lambda")
    void testObserverLambda() {
        List<GameState> states = new ArrayList<>();

        GameObserver lambdaObserver = states::add;

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0
        );

        lambdaObserver.update(state);

        Assertions.assertEquals(1, states.size(), "Lambda observer should work");
    }

    /**
     * Tests that GameObserver can be used with anonymous class.
     */
    @Test
    @DisplayName("GameObserver should work as anonymous class")
    void testObserverAnonymousClass() {
        int[] callCount = new int[1];

        GameObserver anonymousObserver = new GameObserver() {
            @Override
            public void update(GameState state) {
                callCount[0]++;
            }
        };

        Cell[][] field = new Cell[10][10];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                field[x][y] = Cell.EMPTY;
            }
        }

        List<Point> snakeBody = new ArrayList<>();
        snakeBody.add(new Point(5, 5));

        GameState state = new GameState(
                snakeBody, Direction.UP, field, false, 10, 10, false, 0
        );

        anonymousObserver.update(state);

        Assertions.assertEquals(1, callCount[0], "Anonymous observer should work");
    }
}
