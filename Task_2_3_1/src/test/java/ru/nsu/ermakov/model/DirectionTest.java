package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Direction enum.
 * Direction represents the possible movement directions for the snake.
 */
class DirectionTest {

    /**
     * Tests that Direction enum contains all expected values.
     */
    @Test
    @DisplayName("Direction enum should contain all four directions")
    void testDirectionValues() {
        Direction[] directions = Direction.values();
        Assertions.assertEquals(4, directions.length, "There should be exactly 4 directions");
        Assertions.assertArrayEquals(
                new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT},
                directions,
                "Directions should be UP, DOWN, LEFT, RIGHT"
        );
    }

    /**
     * Tests valueOf method for each direction.
     */
    @Test
    @DisplayName("Direction valueOf should work for all directions")
    void testValueOf() {
        Assertions.assertEquals(Direction.UP, Direction.valueOf("UP"), "Should get UP by name");
        Assertions.assertEquals(Direction.DOWN, Direction.valueOf("DOWN"), "Should get DOWN by name");
        Assertions.assertEquals(Direction.LEFT, Direction.valueOf("LEFT"), "Should get LEFT by name");
        Assertions.assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"), "Should get RIGHT by name");
    }

    /**
     * Tests ordinal values of directions.
     */
    @Test
    @DisplayName("Direction ordinals should be as expected")
    void testOrdinals() {
        Assertions.assertEquals(0, Direction.UP.ordinal(), "UP should have ordinal 0");
        Assertions.assertEquals(1, Direction.DOWN.ordinal(), "DOWN should have ordinal 1");
        Assertions.assertEquals(2, Direction.LEFT.ordinal(), "LEFT should have ordinal 2");
        Assertions.assertEquals(3, Direction.RIGHT.ordinal(), "RIGHT should have ordinal 3");
    }

    /**
     * Tests that all directions are unique.
     */
    @Test
    @DisplayName("All directions should be unique")
    void testUniqueDirections() {
        Direction[] directions = Direction.values();
        for (int i = 0; i < directions.length; i++) {
            for (int j = i + 1; j < directions.length; j++) {
                Assertions.assertNotEquals(directions[i], directions[j],
                        "All directions should be unique");
            }
        }
    }
}
