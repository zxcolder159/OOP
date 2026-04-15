package ru.nsu.ermakov.game;

import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.Field;
import ru.nsu.ermakov.model.MoveResult;
import ru.nsu.ermakov.model.Point;

import java.util.LinkedList;

/**
 * Класс, хранящий змейку.
 */
public class Snake {
    final LinkedList<Point> body = new LinkedList<>();
    protected Direction direction;

    /**
     * Конструктор змейки.
     *
     * @param startPoint начальная позиция головы змейки
     */
    public Snake(Point startPoint) {
        body.add(startPoint);
		body.add(new Point(startPoint.x()-1, startPoint.y()));
	    body.add(new Point(startPoint.x()-2, startPoint.y()));
        direction = Direction.UP;
    }

    /**
     * Изменить направление с валидацией (нельзя двигаться в противоположную сторону).
     *
     * @param direction новое направление
     *
     * @return true если направление изменено, false если изменение отклонено
     */
    public boolean changeDirection(Direction direction) {
        if(this.direction == Direction.UP && direction == Direction.DOWN) {
            return false;
        }
        if(this.direction == Direction.DOWN && direction == Direction.UP) {
            return false;
        }
        if(this.direction == Direction.LEFT && direction == Direction.RIGHT) {
            return false;
        }
        if(this.direction == Direction.RIGHT && direction == Direction.LEFT) {
            return false;
        }
        this.direction = direction;
        return true;
    }

    /**
     * Движение змейки.
     *
     * @param field игровое поле
     *
     * @return результат движения
     */
    MoveResult move(Field field) {
        int newX, newY;
        newX = body.getFirst().x();
        newY = body.getFirst().y();
        switch (direction) {
            case UP :
                newY -= 1;
                break;
            case DOWN:
                newY += 1;
                break;
            case LEFT:
                newX -= 1;
                break;
            case RIGHT:
                newX += 1;
                break;
        }
        newX = (newX + field.getWidth()) % field.getWidth();
        newY = (newY + field.getHeight()) % field.getHeight();
        Point newPoint = new Point(newX, newY);
        if(body.contains(newPoint)) {
            return MoveResult.DIED;
        }
        body.addFirst(newPoint);
        if(field.field[newX][newY] == Cell.FOOD) {
            return MoveResult.ATE_FOOD;
        }
        if(field.field[newX][newY] == Cell.WALL) {
            return MoveResult.DIED;
        }
        body.removeLast();
        return MoveResult.MOVED;
    }

    /**
     * Возвращает тело змейки.
     */
    public LinkedList<Point> getBody() {
        return body;
    }

    /**
     * Возвращает направление движения змейки.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Принудительно устанавливает направление без валидации.
     * Используется при рестарте игры.
     *
     * @param direction новое направление
     */
    public void resetDirection(Direction direction) {
        this.direction = direction;
    }
}
