package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MoveResult enum.
 * MoveResult represents the possible outcomes of a snake movement.
 */
class MoveResultTest {

    /**
     * Tests that MoveResult enum contains all expected values.
     */
    @Test
    @DisplayName("MoveResult enum should contain all three results")
    void testMoveResultValues() {
        MoveResult[] results = MoveResult.values();
        Assertions.assertEquals(3, results.length, "There should be exactly 3 move results");
        Assertions.assertArrayEquals(
                new MoveResult[]{MoveResult.MOVED, MoveResult.ATE_FOOD, MoveResult.DIED},
                results,
                "Move results should be MOVED, ATE_FOOD, DIED"
        );
    }

    /**
     * Tests valueOf method for each move result.
     */
    @Test
    @DisplayName("MoveResult valueOf should work for all results")
    void testValueOf() {
        Assertions.assertEquals(MoveResult.MOVED, MoveResult.valueOf("MOVED"), "Should get MOVED by name");
        Assertions.assertEquals(MoveResult.ATE_FOOD, MoveResult.valueOf("ATE_FOOD"), "Should get ATE_FOOD by name");
        Assertions.assertEquals(MoveResult.DIED, MoveResult.valueOf("DIED"), "Should get DIED by name");
    }

    /**
     * Tests ordinal values of move results.
     */
    @Test
    @DisplayName("MoveResult ordinals should be as expected")
    void testOrdinals() {
        Assertions.assertEquals(0, MoveResult.MOVED.ordinal(), "MOVED should have ordinal 0");
        Assertions.assertEquals(1, MoveResult.ATE_FOOD.ordinal(), "ATE_FOOD should have ordinal 1");
        Assertions.assertEquals(2, MoveResult.DIED.ordinal(), "DIED should have ordinal 2");
    }

    /**
     * Tests that all move results are unique.
     */
    @Test
    @DisplayName("All move results should be unique")
    void testUniqueMoveResults() {
        MoveResult[] results = MoveResult.values();
        for (int i = 0; i < results.length; i++) {
            for (int j = i + 1; j < results.length; j++) {
                Assertions.assertNotEquals(results[i], results[j],
                        "All move results should be unique");
            }
        }
    }

    /**
     * Tests MOVED result meaning.
     */
    @Test
    @DisplayName("MOVED represents successful movement")
    void testMovedResult() {
        MoveResult result = MoveResult.MOVED;
        Assertions.assertEquals("MOVED", result.name(), "Name should be MOVED");
    }

    /**
     * Tests ATE_FOOD result meaning.
     */
    @Test
    @DisplayName("ATE_FOOD represents eating food")
    void testAteFoodResult() {
        MoveResult result = MoveResult.ATE_FOOD;
        Assertions.assertEquals("ATE_FOOD", result.name(), "Name should be ATE_FOOD");
    }

    /**
     * Tests DIED result meaning.
     */
    @Test
    @DisplayName("DIED represents snake death")
    void testDiedResult() {
        MoveResult result = MoveResult.DIED;
        Assertions.assertEquals("DIED", result.name(), "Name should be DIED");
    }
}
