package ru.nsu.ermakov.model;

/**
 * Класс, представляющий уровень игры.
 */
public class Level {
    private final String name;
    private final Cell[][] field;
    private final Point startPoint;
    private final String description;

    /**
     * Конструктор уровня.
     *
     * @param name название уровня
     * @param field игровое поле уровня
     * @param startPoint начальная точка змейки
     * @param description описание уровня
     */
    public Level(String name, Cell[][] field, Point startPoint, String description) {
        this.name = name;
        this.field = field;
        this.startPoint = startPoint;
        this.description = description;
    }

    /**
     * Возвращает название уровня.
     *
     * @return название уровня
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает игровое поле уровня.
     *
     * @return игровое поле
     */
    public Cell[][] getField() {
        return field;
    }

    /**
     * Возвращает начальную точку змейки.
     *
     * @return начальная точка
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * Возвращает описание уровня.
     *
     * @return описание уровня
     */
    public String getDescription() {
        return description;
    }
}
