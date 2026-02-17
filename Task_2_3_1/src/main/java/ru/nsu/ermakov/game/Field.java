package ru.nsu.ermakov.game;

/**
 * Поле для змейки.
 */
public class Field {
    public final int width;
    public final int height;

    Cell[][] field;

    /**
     * Конструктор, как трек бабангиды.
     *
     * @param width
     *
     * @param height
     */
    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new Cell[this.width][this.height];
        clear();
    }

    /**
     * Геттер ширины.
     */
    public int getWidth() {
        return width;
    }


    /**
     * Геттер высоты.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Очищает поле.
     */
    void clear() {
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
    }

    public Cell getCell(int x, int y) {
        return field[x][y];
    }
}
