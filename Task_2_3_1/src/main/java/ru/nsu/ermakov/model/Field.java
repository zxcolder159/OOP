package ru.nsu.ermakov.model;

/**
 * Поле для змейки.
 */
public class Field {
    private final int width;
    private final int height;

    private final Cell[][] field;

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
    private void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
    }

    /**
     * Возвращает ячейку поля по координатам.
     *
     * @param x координата X
     * @param y координата Y
     * @return ячейка поля
     */
    public Cell getCell(int x, int y) {
        return field[x][y];
    }

    /**
     * Устанавливает значение ячейки по координатам.
     *
     * @param x координата X
     * @param y координата Y
     * @param cell новое значение ячейки
     */
    public void setCell(int x, int y, Cell cell) {
        field[x][y] = cell;
    }

    /**
     * Копирует все ячейки из переданного массива на текущее поле.
     *
     * @param source источник данных поля
     */
    public void copyFrom(Cell[][] source) {
        for (int i = 0; i < width; i++) {
            System.arraycopy(source[i], 0, field[i], 0, height);
        }
    }

    /**
     * Очищает все ячейки заданного типа.
     *
     * @param cellType тип ячейки для удаления
     */
    public void clearCellsOfType(Cell cellType) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field[i][j] == cellType) {
                    field[i][j] = Cell.EMPTY;
                }
            }
        }
    }

    /**
     * Возвращает полную копию поля.
     */
    public Cell[][] copyCells() {
        Cell[][] copy = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            System.arraycopy(field[i], 0, copy[i], 0, height);
        }
        return copy;
    }
}
