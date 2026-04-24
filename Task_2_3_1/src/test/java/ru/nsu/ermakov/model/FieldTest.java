package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Field class.
 * Field represents the game board where the snake moves.
 */
class FieldTest {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    private Field field;

    /**
     * Sets up a fresh field before each test.
     */
    @BeforeEach
    void setUp() {
        field = new Field(WIDTH, HEIGHT);
    }

    /**
     * Tests field dimensions are set correctly.
     */
    @Test
    @DisplayName("Field should have correct width and height")
    void testFieldDimensions() {
        Assertions.assertEquals(WIDTH, field.getWidth(),
                "Width should match constructor parameter");
        Assertions.assertEquals(HEIGHT, field.getHeight(),
                "Height should match constructor parameter");
    }

    /**
     * Tests that field is initialized with EMPTY cells.
     */
    @Test
    @DisplayName("Field should be initialized with all EMPTY cells")
    void testFieldInitializedEmpty() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Assertions.assertEquals(Cell.EMPTY, field.getCell(x, y),
                        "Cell at (" + x + "," + y + ") should be EMPTY");
            }
        }
    }

    /**
     * Tests setting and getting cells.
     */
    @Test
    @DisplayName("Field should allow setting and getting cells")
    void testSetAndGetCell() {
        field.setCell(5, 5, Cell.FOOD);
        Assertions.assertEquals(Cell.FOOD, field.getCell(5, 5),
                "Cell should be FOOD after setting");

        field.setCell(3, 4, Cell.WALL);
        Assertions.assertEquals(Cell.WALL, field.getCell(3, 4),
                "Cell should be WALL after setting");
    }

    /**
     * Tests repeated cell updates via API.
     */
    @Test
    @DisplayName("Field should be modifiable through public API")
    void testFieldModificationViaApi() {
        field.setCell(0, 0, Cell.SNAKE);
        Assertions.assertEquals(Cell.SNAKE, field.getCell(0, 0), "Cell should be updated to SNAKE");
    }

    /**
     * Tests field with minimum dimensions.
     */
    @Test
    @DisplayName("Field should work with minimum dimensions")
    void testMinimumDimensions() {
        Field smallField = new Field(1, 1);
        Assertions.assertEquals(1, smallField.getWidth(), "Minimum width should be 1");
        Assertions.assertEquals(1, smallField.getHeight(), "Minimum height should be 1");
        Assertions.assertEquals(Cell.EMPTY, smallField.getCell(0, 0),
                "Single cell should be EMPTY");
    }

    /**
     * Tests field with larger dimensions.
     */
    @Test
    @DisplayName("Field should work with larger dimensions")
    void testLargerDimensions() {
        Field largeField = new Field(50, 50);
        Assertions.assertEquals(50, largeField.getWidth(), "Large width should be set correctly");
        Assertions.assertEquals(50, largeField.getHeight(), "Large height should be set correctly");

        largeField.setCell(25, 25, Cell.FOOD);
        Assertions.assertEquals(Cell.FOOD, largeField.getCell(25, 25),
                "Cell in large field should be accessible");
    }
}
