package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Cell enum.
 * Cell represents the possible states of a cell on the game field.
 */
class CellTest {

    /**
     * Tests that Cell enum contains all expected values.
     */
    @Test
    @DisplayName("Cell enum should contain all four cell types")
    void testCellValues() {
        Cell[] cells = Cell.values();
        Assertions.assertEquals(4, cells.length, "There should be exactly 4 cell types");
        Assertions.assertArrayEquals(
                new Cell[]{Cell.EMPTY, Cell.SNAKE, Cell.FOOD, Cell.WALL},
                cells,
                "Cells should be EMPTY, SNAKE, FOOD, WALL"
        );
    }

    /**
     * Tests valueOf method for each cell type.
     */
    @Test
    @DisplayName("Cell valueOf should work for all cell types")
    void testValueOf() {
        Assertions.assertEquals(Cell.EMPTY, Cell.valueOf("EMPTY"), "Should get EMPTY by name");
        Assertions.assertEquals(Cell.SNAKE, Cell.valueOf("SNAKE"), "Should get SNAKE by name");
        Assertions.assertEquals(Cell.FOOD, Cell.valueOf("FOOD"), "Should get FOOD by name");
        Assertions.assertEquals(Cell.WALL, Cell.valueOf("WALL"), "Should get WALL by name");
    }

    /**
     * Tests ordinal values of cells.
     */
    @Test
    @DisplayName("Cell ordinals should be as expected")
    void testOrdinals() {
        Assertions.assertEquals(0, Cell.EMPTY.ordinal(), "EMPTY should have ordinal 0");
        Assertions.assertEquals(1, Cell.SNAKE.ordinal(), "SNAKE should have ordinal 1");
        Assertions.assertEquals(2, Cell.FOOD.ordinal(), "FOOD should have ordinal 2");
        Assertions.assertEquals(3, Cell.WALL.ordinal(), "WALL should have ordinal 3");
    }

    /**
     * Tests that all cell types are unique.
     */
    @Test
    @DisplayName("All cell types should be unique")
    void testUniqueCells() {
        Cell[] cells = Cell.values();
        for (int i = 0; i < cells.length; i++) {
            for (int j = i + 1; j < cells.length; j++) {
                Assertions.assertNotEquals(cells[i], cells[j],
                        "All cell types should be unique");
            }
        }
    }
}
