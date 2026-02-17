package ru.nsu.ermakov.game;

public class Game {
    Field field;
    Snake snake;
    boolean isGameOver = false;
    /**
     * Конструктор, как трек бабангиды.
     *
     * @param field
     */
    public Game (Cell[][] field, Point point) {
        this.field = new Field(field.length, field[0].length);
        this.snake = new Snake(point); // <--- Кстати, тут лучше добавить this для порядка
        for(int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, this.field.field[i], 0, field[0].length);
        }
        spawnFood();
    }

    /**
     * Метод делающий шаг.
     */
    public void step() {
        if(isGameOver) {
            return;
        }
        MoveResult moveResult = snake.move(field);
        if(moveResult == MoveResult.DIED) {
            isGameOver = true;
        }
        if(moveResult == MoveResult.ATE_FOOD) {
            field.field[snake.body.getFirst().x()][snake.body.getFirst().y()] = Cell.EMPTY;
            spawnFood();
        }
    }

    /**
     * Проверяет можно ли заспавнить еду по рандомным координатом, и спаунит если можно.
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
     * Получаем рандомное значение на поле.
     */
    private Point getRandomCord () {
        int randomNumberX = (int) (Math.random() * field.width);
        int randomNumberY = (int) (Math.random() * field.height);
        return new Point(randomNumberX, randomNumberY);
    }

    /**
     * Геттер, состояния игры.
     */
    public boolean isGameOver() {
        return isGameOver;
    }
    /**
     * Геттер, змеи.
     */
    public Snake getSnake() {
        return snake;
    }
    /**
     * Геттер, поля.
     */
    public Field getField() {
        return field;
    }
}
