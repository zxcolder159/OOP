package ru.nsu.ermakov.model;

import ru.nsu.ermakov.model.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер уровней игры.
 */
public class LevelManager {
    private static final List<Level> levels = new ArrayList<>();
    private static int selectedLevelIndex = 0;

    static {
        int width = 20;
        int height = 15;


        Cell[][] level1Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level1Field[i][j] = Cell.EMPTY;
            }
        }
        levels.add(new Level("Level 1: Classic", level1Field, new Point(10, 7), "Empty field, classic snake"));


        Cell[][] level2Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level2Field[i][j] = Cell.EMPTY;
            }
        }
        for (int i = 8; i <= 11; i++) {
            level2Field[i][6] = Cell.WALL;
            level2Field[i][8] = Cell.WALL;
        }
        levels.add(new Level("Level 2: Center Walls", level2Field, new Point(10, 7), "Walls in the center"));


        Cell[][] level3Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level3Field[i][j] = Cell.EMPTY;
            }
        }
        for (int i = 2; i <= 5; i++) {
            level3Field[2][i] = Cell.WALL;
            level3Field[17][i] = Cell.WALL;
            level3Field[2][height - 1 - i] = Cell.WALL;
            level3Field[17][height - 1 - i] = Cell.WALL;
        }
        levels.add(new Level("Level 3: Corners", level3Field, new Point(10, 7), "Walls in corners"));


        Cell[][] level4Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level4Field[i][j] = Cell.EMPTY;
            }
        }
        for (int i = 0; i < width; i++) {
            level4Field[i][7] = Cell.WALL;
        }
        for (int j = 0; j < height; j++) {
            level4Field[10][j] = Cell.WALL;
        }
        level4Field[10][7] = Cell.EMPTY;
        levels.add(new Level("Level 4: Cross", level4Field, new Point(10, 6), "Cross pattern walls"));


        Cell[][] level5Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level5Field[i][j] = Cell.EMPTY;
            }
        }
        for (int i = 5; i <= 14; i++) {
            level5Field[i][3] = Cell.WALL;
            level5Field[i][11] = Cell.WALL;
        }
        for (int j = 4; j <= 10; j++) {
            level5Field[5][j] = Cell.WALL;
            level5Field[14][j] = Cell.WALL;
        }
        level5Field[9][7] = Cell.WALL;
        levels.add(new Level("Level 5: Maze", level5Field, new Point(10, 7), "Maze-like obstacles"));


        Cell[][] level6Field = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level6Field[i][j] = Cell.EMPTY;
            }
        }
        int[][] wallPositions = {
            {3, 3}, {3, 11}, {16, 3}, {16, 11},
            {7, 5}, {7, 9}, {12, 5}, {12, 9},
            {9, 4}, {9, 10}, {5, 7}, {14, 7}
        };
        for (int[] pos : wallPositions) {
            level6Field[pos[0]][pos[1]] = Cell.WALL;
        }
        levels.add(new Level("Level 6: Scattered", level6Field, new Point(10, 7), "Scattered obstacles"));
    }

    /**
     * Возвращает список всех уровней.
     *
     * @return список уровней
     */
    public static List<Level> getLevels() {
        return levels;
    }

    /**
     * Возвращает выбранный уровень.
     *
     * @return выбранный уровень
     */
    public static Level getSelectedLevel() {
        return levels.get(selectedLevelIndex);
    }

    /**
     * Выбирает следующий уровень.
     */
    public static void selectNextLevel() {
        selectedLevelIndex = (selectedLevelIndex + 1) % levels.size();
    }

    /**
     * Выбирает предыдущий уровень.
     */
    public static void selectPreviousLevel() {
        selectedLevelIndex = (selectedLevelIndex - 1 + levels.size()) % levels.size();
    }

    /**
     * Устанавливает индекс выбранного уровня.
     *
     * @param index индекс уровня
     */
    public static void setSelectedLevelIndex(int index) {
        if (index >= 0 && index < levels.size()) {
            selectedLevelIndex = index;
        }
    }

    /**
     * Возвращает индекс выбранного уровня.
     *
     * @return индекс уровня
     */
    public static int getSelectedLevelIndex() {
        return selectedLevelIndex;
    }
}
