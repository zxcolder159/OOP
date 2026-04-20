package ru.nsu.ermakov.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.Field;
import ru.nsu.ermakov.model.MoveResult;
import ru.nsu.ermakov.model.Point;
import java.util.LinkedList;

/**
 * Tests for the Snake class.
 * Snake represents the snake entity with movement and direction change logic.
 */
class SnakeTest {

    private static final int FIELD_WIDTH = 20;
    private static final int FIELD_HEIGHT = 15;

    private Field field;

    /**
     * Sets up a fresh field before each test.
     */
    @BeforeEach
    void setUp() {
        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
    }

    /**
     * Tests that snake is created with correct initial body.
     */
    @Test
    @DisplayName("Snake should be created with initial body of 3 segments")
    void testSnakeCreation() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        LinkedList<Point> body = snake.getBody();
        Assertions.assertEquals(3, body.size(), "Snake should have 3 body segments");
        Assertions.assertEquals(startPoint, body.get(0), "Head should be at start point");
        Assertions.assertEquals(new Point(9, 7), body.get(1),
                "Second segment should be below head");
        Assertions.assertEquals(new Point(8, 7), body.get(2),
                "Third segment should be below second");
    }

    /**
     * Tests that initial direction is UP.
     */
    @Test
    @DisplayName("Snake should start with UP direction")
    void testInitialDirection() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        Assertions.assertEquals(Direction.UP, snake.getDirection(),
                "Initial direction should be UP");
    }

    /**
     * Tests changing direction to valid direction (perpendicular).
     */
    @Test
    @DisplayName("Snake should change to perpendicular directions")
    void testChangeDirectionPerpendicular() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        boolean result = snake.changeDirection(Direction.LEFT);
        Assertions.assertTrue(result, "Should be able to change from UP to LEFT");
        Assertions.assertEquals(Direction.LEFT, snake.getDirection(), "Direction should be LEFT");

        result = snake.changeDirection(Direction.DOWN);
        Assertions.assertTrue(result, "Should be able to change from LEFT to DOWN");
        Assertions.assertEquals(Direction.DOWN, snake.getDirection(), "Direction should be DOWN");

        result = snake.changeDirection(Direction.RIGHT);
        Assertions.assertTrue(result, "Should be able to change from DOWN to RIGHT");
        Assertions.assertEquals(Direction.RIGHT, snake.getDirection(), "Direction should be RIGHT");

        result = snake.changeDirection(Direction.UP);
        Assertions.assertTrue(result, "Should be able to change from RIGHT to UP");
        Assertions.assertEquals(Direction.UP, snake.getDirection(), "Direction should be UP");
    }

    /**
     * Tests that snake cannot move in opposite direction from UP.
     */
    @Test
    @DisplayName("Snake should not be able to move opposite to UP")
    void testCannotMoveOppositeToUp() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        boolean result = snake.changeDirection(Direction.DOWN);
        Assertions.assertFalse(result, "Should not be able to change from UP to DOWN");
        Assertions.assertEquals(Direction.UP, snake.getDirection(), "Direction should remain UP");
    }

    /**
     * Tests that snake cannot move in opposite direction from DOWN.
     */
    @Test
    @DisplayName("Snake should not be able to move opposite to DOWN")
    void testCannotMoveOppositeToDown() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        snake.changeDirection(Direction.LEFT);
        snake.changeDirection(Direction.DOWN);
        Assertions.assertEquals(Direction.DOWN, snake.getDirection(), "Direction should be DOWN");

        boolean result = snake.changeDirection(Direction.UP);
        Assertions.assertFalse(result, "Should not be able to change from DOWN to UP");
        Assertions.assertEquals(Direction.DOWN, snake.getDirection(),
                "Direction should remain DOWN");
    }

    /**
     * Tests that snake cannot move in opposite direction from LEFT.
     */
    @Test
    @DisplayName("Snake should not be able to move opposite to LEFT")
    void testCannotMoveOppositeToLeft() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        snake.changeDirection(Direction.LEFT);
        Assertions.assertEquals(Direction.LEFT, snake.getDirection(), "Direction should be LEFT");

        boolean result = snake.changeDirection(Direction.RIGHT);
        Assertions.assertFalse(result, "Should not be able to change from LEFT to RIGHT");
        Assertions.assertEquals(Direction.LEFT, snake.getDirection(),
                "Direction should remain LEFT");
    }

    /**
     * Tests that snake cannot move in opposite direction from RIGHT.
     */
    @Test
    @DisplayName("Snake should not be able to move opposite to RIGHT")
    void testCannotMoveOppositeToRight() {
        Point startPoint = new Point(10, 7);
        Snake snake = new Snake(startPoint);

        snake.changeDirection(Direction.RIGHT);
        Assertions.assertEquals(Direction.RIGHT, snake.getDirection(), "Direction should be RIGHT");

        boolean result = snake.changeDirection(Direction.LEFT);
        Assertions.assertFalse(result, "Should not be able to change from RIGHT to LEFT");
        Assertions.assertEquals(Direction.RIGHT, snake.getDirection(),
                "Direction should remain RIGHT");
    }

    /**
     * Tests snake moving UP.
     */
    @Test
    @DisplayName("Snake should move UP correctly")
    void testMoveUp() {
        Point startPoint = new Point(10, 10);
        Snake snake = new Snake(startPoint);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.MOVED, result, "Should have moved");
        Assertions.assertEquals(new Point(10, 9), snake.getBody().getFirst(),
                "Head should move up");
        Assertions.assertEquals(3, snake.getBody().size(),
                "Size should remain 3 after normal move");
    }

    /**
     * Tests snake moving RIGHT.
     */
    @Test
    @DisplayName("Snake should move RIGHT correctly")
    void testMoveRight() {
        Point startPoint = new Point(10, 10);
        Snake snake = new Snake(startPoint);
        snake.changeDirection(Direction.RIGHT);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.MOVED, result, "Should have moved");
        Assertions.assertEquals(new Point(11, 10), snake.getBody().getFirst(),
                "Head should move right");
    }

    /**
     * Tests snake wrapping around field edges (UP).
     */
    @Test
    @DisplayName("Snake should wrap around when moving UP past top edge")
    void testWrapAroundUp() {
        Point startPoint = new Point(10, 0);
        Snake snake = new Snake(startPoint);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.MOVED, result, "Should have moved");
        Assertions.assertEquals(new Point(10, FIELD_HEIGHT - 1), snake.getBody().getFirst(),
                "Head should wrap to bottom");
    }


    /**
     * Tests snake wrapping around field edges (LEFT).
     */
    @Test
    @DisplayName("Snake should wrap around when moving LEFT past left edge")
    void testWrapAroundLeft() {
        Point startPoint = new Point(0, 10);
        Snake snake = new Snake(startPoint);
        snake.changeDirection(Direction.LEFT);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.MOVED, result, "Should have moved");
        Assertions.assertEquals(new Point(FIELD_WIDTH - 1, 10), snake.getBody().getFirst(),
                "Head should wrap to right");
    }

    /**
     * Tests snake wrapping around field edges (RIGHT).
     */
    @Test
    @DisplayName("Snake should wrap around when moving RIGHT past right edge")
    void testWrapAroundRight() {
        Point startPoint = new Point(FIELD_WIDTH - 1, 10);
        Snake snake = new Snake(startPoint);
        snake.changeDirection(Direction.RIGHT);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.MOVED, result, "Should have moved");
        Assertions.assertEquals(new Point(0, 10), snake.getBody().getFirst(),
                "Head should wrap to left");
    }



    /**
     * Tests snake eating food.
     */
    @Test
    @DisplayName("Snake should eat food and grow")
    void testEatFood() {
        Point startPoint = new Point(10, 10);
        Snake snake = new Snake(startPoint);

        field.setCell(10, 9, Cell.FOOD);

        int sizeBefore = snake.getBody().size();
        MoveResult result = snake.move(field);
        int sizeAfter = snake.getBody().size();

        Assertions.assertEquals(MoveResult.ATE_FOOD, result, "Should return ATE_FOOD");
        Assertions.assertEquals(sizeBefore + 1, sizeAfter, "Snake should grow after eating");
    }

    /**
     * Tests snake dying when hitting a wall.
     */
    @Test
    @DisplayName("Snake should die when hitting a wall")
    void testHitWall() {
        Point startPoint = new Point(10, 10);
        Snake snake = new Snake(startPoint);

        field.setCell(10, 9, Cell.WALL);

        MoveResult result = snake.move(field);

        Assertions.assertEquals(MoveResult.DIED, result, "Snake should die from wall collision");
    }

    /**
     * Tests snake body follows the head correctly.
     */
    @Test
    @DisplayName("Snake body should follow head movement")
    void testBodyFollowsHead() {
        Point startPoint = new Point(10, 10);
        Snake snake = new Snake(startPoint);

        snake.move(field);
        LinkedList<Point> body = snake.getBody();

        Assertions.assertEquals(new Point(10, 9), body.get(0), "Head should be at new position");
        Assertions.assertEquals(startPoint, body.get(1),
                "First body segment should be at old head position");
    }
}
