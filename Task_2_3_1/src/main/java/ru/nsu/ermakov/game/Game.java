package ru.nsu.ermakov.game;

import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.Field;
import ru.nsu.ermakov.model.GameObserver;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.model.MoveResult;
import ru.nsu.ermakov.model.Point;

import java.util.ArrayList;
import java.util.List;

public class Game {
    Field field;
    Snake snake;
    boolean isGameOver = false;
    boolean isPaused = false;
    int score = 0;
    private long moveIntervalNanos = 100_000_000L;
    private Direction lastMoveDirection = Direction.UP;
    private static final int INITIAL_SNAKE_SIZE = 3;

	private final List<GameObserver> observers = new ArrayList<>();
    /**
     * Конструктор игры.
     *
     * @param field игровое поле
     * @param point начальная позиция змейки
     */
    public Game (Cell[][] field, Point point) {
        this.field = new Field(field.length, field[0].length);
        this.snake = new Snake(point);
        for(int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, this.field.field[i], 0, field[0].length);
        }
        spawnFood();
    }

    /**
     * Проверить, находится ли игра на паузе.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Получить текущий счет (размер змеи - начальный размер).
     */
    public int getScore() {
        return score;
    }

    /**
     * Получить интервал между шагами змейки в наносекундах.
     */
    public long getMoveIntervalNanos() {
        return moveIntervalNanos;
    }

    /**
     * Установить интервал между шагами змейки.
     * Меньше значение = быстрее змейка.
     *
     * @param intervalNanos интервал в наносекундах
     */
    public void setMoveIntervalNanos(long intervalNanos) {
        if (intervalNanos > 0) {
            this.moveIntervalNanos = intervalNanos;
        }
    }

    /**
     * Увеличить скорость змейки (уменьшить интервал).
     *
     * @param deltaNanos на сколько уменьшить интервал
     */
    public void increaseSpeed(long deltaNanos) {
        long newInterval = moveIntervalNanos - deltaNanos;
        if (newInterval < 50_000_000L) {
            newInterval = 50_000_000L;
        }
        moveIntervalNanos = newInterval;
    }

    /**
     * Спавнит еду на случайной пустой клетке.
     */
    private void spawnFood () {

        while(true) {
            Point point = getRandomCord();
            if(field.field[point.x()][point.y()] == Cell.EMPTY && !snake.body.contains(point)) {
                field.field[point.x()][point.y()] = Cell.FOOD;
                break;
            }
        }
    }

    /**
     * Возвращает случайные координаты на поле.
     */
    private Point getRandomCord () {
        int randomNumberX = (int) (Math.random() * field.width);
        int randomNumberY = (int) (Math.random() * field.height);
        return new Point(randomNumberX, randomNumberY);
    }

    /**
     * Метод для изменения направления змейки с валидацией.
     *
     * @param direction новое направление
     * @return true если направление изменено, false если изменение отклонено
     */
    public boolean changeSnakeDirection(Direction direction) {
        return snake.changeDirection(direction);
    }

    /**
     * Возвращает текущее состояние игры для отображения.
     *
     * @return объект GameState с копией данных
     */
    public GameState getState() {
        List<Point> bodyCopy = new ArrayList<>(snake.getBody());
        Cell[][] fieldCopy = new Cell[field.width][field.height];
        for (int i = 0; i < field.width; i++) {
            System.arraycopy(field.field[i], 0, fieldCopy[i], 0, field.height);
        }
        return new GameState(bodyCopy, lastMoveDirection, fieldCopy, isGameOver, field.width, field.height, isPaused, score);
    }

    /**
     * Проверяет, закончена ли игра.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

	public void addObserver(GameObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(GameObserver observer) {
		observers.remove(observer);
	}

	private void notifyObservers() {
		GameState state = getState();
		for (GameObserver observer : observers) {
			observer.update(state);
		}
	}

	public void step() {
		if(isGameOver || isPaused) {
			return;
		}
		lastMoveDirection = snake.getDirection();
		MoveResult moveResult = snake.move(field);

		if(moveResult == MoveResult.DIED) {
			isGameOver = true;
		}
		if(moveResult == MoveResult.ATE_FOOD) {
			field.field[snake.body.getFirst().x()][snake.body.getFirst().y()] = Cell.EMPTY;
			spawnFood();
			score++;
			increaseSpeed(2_000_000L);
		}

		notifyObservers();
	}

	public void togglePause() {
		if (!isGameOver) {
			isPaused = !isPaused;

			notifyObservers();
		}
	}
}
