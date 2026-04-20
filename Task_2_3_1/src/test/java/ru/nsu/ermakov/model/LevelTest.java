package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Level.
 */
public class LevelTest {
    /**
     * Проверяет корректность конструктора и геттеров.
     */
    @Test
    @DisplayName("Level constructor and getters work correctly")
    public void testConstructorAndGetters() {
        Cell[][] field = new Cell[2][2];
        field[0][0] = Cell.EMPTY;
        field[0][1] = Cell.WALL;
        field[1][0] = Cell.FOOD;
        field[1][1] = Cell.SNAKE;
        Point start = new Point(1, 1);
        String desc = "desc";
        Level level = new Level("name", field, start, desc);
        Assertions.assertEquals("name", level.getName());
        Assertions.assertEquals(field, level.getField());
        Assertions.assertEquals(start, level.getStartPoint());
        Assertions.assertEquals(desc, level.getDescription());
    }
}

