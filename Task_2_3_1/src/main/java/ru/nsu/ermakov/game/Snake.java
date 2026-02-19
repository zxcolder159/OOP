package ru.nsu.ermakov.game;

import java.util.LinkedList;

/**
 * Класс, хранящий змейку.
 */
public class Snake {
    final LinkedList<Point> body = new LinkedList<>();
    Direction direction;

    /**
     * Конструктор, как трек бабангиды.
     *
     * @param startPoint
     */
    public Snake(Point startPoint) {
        body.add(startPoint);
        direction = Direction.UP;
    }

    /**
     * Изменить направление.
     *
     * @param direction
     *
     */
    void changeDirection(Direction direction) {
        if(this.direction == Direction.UP && direction == Direction.DOWN) {
            return;
        }
        if(this.direction == Direction.DOWN && direction == Direction.UP) {
            return;
        }
        if(this.direction == Direction.LEFT && direction == Direction.RIGHT) {
            return;
        }
        if(this.direction == Direction.RIGHT && direction == Direction.LEFT) {
            return;
        }
        this.direction = direction;
    }

    /**
     * Движение змейки.
     */
    MoveResult move(Field field) {
        int newX, newY;
        newX = body.getFirst().x();
        newY = body.getFirst().y();
        //Свитч, как учил Хаверко(мастер снюса).
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
     * Геттер тела змеи.
     */
    public LinkedList<Point> getBody() {
        return body;
    }

    /**
     * Геттер направления.
     */
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
