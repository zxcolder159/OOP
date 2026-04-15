package ru.nsu.ermakov.game;

import ru.nsu.ermakov.model.AppState;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Level;
import ru.nsu.ermakov.model.LevelManager;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.Field;
import ru.nsu.ermakov.model.GameObserver;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.model.MoveResult;
import ru.nsu.ermakov.model.Point;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Field field;
    private final Snake snake;
    boolean isGameOver = false;
    boolean isPaused = false;
    int score = 0;
    private long moveIntervalNanos = 100_000_000L;
    private Direction lastMoveDirection = Direction.UP;
    private static final int INITIAL_SNAKE_SIZE = 3;
    private AppState appState = AppState.MENU;
    private final Level level;
    private final Cell[][] originalField;
    private final Point originalStartPoint;

	private final List<GameObserver> observers = new ArrayList<>();
    /**
     * Конструктор игры.
     *
     * @param field игровое поле
     *
     * @param point начальная позиция змейки
     */
    public Game (Cell[][] field, Point point) {
        this.level = null;
        this.originalField = new Cell[field.length][field[0].length];
        for(int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, this.originalField[i], 0, field[0].length);
        }
        this.originalStartPoint = point;
        this.field = new Field(field.length, field[0].length);
        this.snake = new Snake(point);
        for(int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, this.field.field[i], 0, field[0].length);
        }
        spawnFood();
    }

    /**
     * Конструктор игры на основе уровня.
     *
     * @param level уровень игры
     */
    public Game(Level level) {
        this.level = level;
        this.originalField = null;
        this.originalStartPoint = null;
        Cell[][] levelField = level.getField();
        this.field = new Field(levelField.length, levelField[0].length);
        this.snake = new Snake(level.getStartPoint());
        for(int i = 0; i < levelField.length; i++) {
            System.arraycopy(levelField[i], 0, this.field.field[i], 0, levelField[0].length);
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
     *
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
        return new GameState(bodyCopy, lastMoveDirection, fieldCopy,
                             isGameOver, field.width, field.height, isPaused, score, appState);
    }

    /**
     * Проверяет, закончена ли игра.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

	/**
	 * Добавляет наблюдателя игры.
	 *
	 * @param observer наблюдатель
	 */
	public void addObserver(GameObserver observer) {
		observers.add(observer);
	}

	/**
	 * Удаляет наблюдателя игры.
	 *
	 * @param observer наблюдатель
	 */
	public void removeObserver(GameObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Уведомляет всех наблюдателей об изменении состояния.
	 */
	public void notifyObservers() {
		GameState state = getState();
		for (GameObserver observer : observers) {
			observer.update(state);
		}
	}

	/**
	 * Выполняет один шаг игры.
	 */
	public void step() {
		if(isGameOver || isPaused || appState != AppState.PLAYING) {
			return;
		}
		lastMoveDirection = snake.getDirection();
		MoveResult moveResult = snake.move(field);

		if(moveResult == MoveResult.DIED) {
			isGameOver = true;
			appState = AppState.GAME_OVER;
		}
		if(moveResult == MoveResult.ATE_FOOD) {
			field.field[snake.body.getFirst().x()][snake.body.getFirst().y()] = Cell.EMPTY;
			spawnFood();
			score++;
			increaseSpeed(2_000_000L);
		}

		notifyObservers();
	}

	/**
	 * Переключает состояние паузы.
	 */
	public void togglePause() {
		if (!isGameOver) {
			isPaused = !isPaused;
			if (isPaused) {
				appState = AppState.PAUSED;
			} else {
				appState = AppState.PLAYING;
			}
			notifyObservers();
		}
	}

	/**
	 * Устанавливает состояние приложения.
	 *
	 * @param state состояние приложения
	 */
	public void setAppState(AppState state) {
		this.appState = state;
		notifyObservers();
	}

	/**
	 * Загружает уровень.
	 */
	private void loadLevel() {
		if (level != null) {
			Level newLevel = LevelManager.getSelectedLevel();
			Cell[][] newField = newLevel.getField();

			for (int i = 0; i < field.width; i++) {
				System.arraycopy(newField[i], 0, this.field.field[i], 0, field.height);
			}

			Point startPoint = newLevel.getStartPoint();
			snake.body.clear();
			snake.body.add(startPoint);
			snake.body.add(new Point(startPoint.x(), startPoint.y() + 1));
			snake.body.add(new Point(startPoint.x(), startPoint.y() + 2));
			snake.resetDirection(Direction.UP);

			for (int i = 0; i < field.width; i++) {
				for (int j = 0; j < field.height; j++) {
					if (field.field[i][j] == Cell.FOOD) {
						field.field[i][j] = Cell.EMPTY;
					}
				}
			}
			spawnFood();

			String levelName = newLevel.getName();
			if (levelName.contains("Easy")) {
				moveIntervalNanos = 150_000_000L;
			} else if (levelName.contains("Hard")) {
				moveIntervalNanos = 70_000_000L;
			} else {
				moveIntervalNanos = 100_000_000L;
			}
		} else {
			for (int i = 0; i < field.width; i++) {
				System.arraycopy(originalField[i], 0, this.field.field[i], 0, field.height);
			}

			snake.body.clear();
			snake.body.add(originalStartPoint);
			snake.body.add(new Point(originalStartPoint.x(), originalStartPoint.y() + 1));
			snake.body.add(new Point(originalStartPoint.x(), originalStartPoint.y() + 2));
			snake.resetDirection(Direction.UP);

			for (int i = 0; i < field.width; i++) {
				for (int j = 0; j < field.height; j++) {
					if (field.field[i][j] == Cell.FOOD) {
						field.field[i][j] = Cell.EMPTY;
					}
				}
			}
			spawnFood();

			moveIntervalNanos = 100_000_000L;
		}
	}

	/**
	 * Возвращает состояние приложения.
	 *
	 * @return состояние приложения
	 */
	public AppState getAppState() {
		return appState;
	}

	/**
	 * Запускает игру.
	 */
	public void startGame() {
		loadLevel();
		appState = AppState.PLAYING;
		isGameOver = false;
		isPaused = false;
		notifyObservers();
	}

	/**
	 * Перезапускает игру.
	 */
	public void restart() {
		loadLevel();
		isGameOver = false;
		isPaused = false;
		score = 0;
		lastMoveDirection = Direction.UP;
		appState = AppState.PLAYING;
		notifyObservers();
	}
}
