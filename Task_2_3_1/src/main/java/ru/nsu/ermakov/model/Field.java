package ru.nsu.ermakov.model;

/**
 * Поле для змейки.
 */
public class Field {
    public final int width;
    public final int height;

    public Cell[][] field;

    /**
     * Конструктор поля.
     *
     * @param width ширина поля
     *
     * @param height высота поля
     */
    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        field = new Cell[this.width][this.height];
        clear();
    }

    /**
     * Возвращает ширину поля.
     */
    public int getWidth() {
        return width;
    }


    /**
     * Возвращает высоту поля.
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

    /**
     * Возвращает ячейку поля по координатам.
     *
     * @param x координата X
     *
     * @param y координата Y
     *
     * @return ячейка поля
     */
    public Cell getCell(int x, int y) {
        return field[x][y];
    }
}
