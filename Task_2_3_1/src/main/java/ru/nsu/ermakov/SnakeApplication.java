package ru.nsu.ermakov;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import ru.nsu.ermakov.game.*;
import ru.nsu.ermakov.gui.Assets;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;

public class SnakeApplication extends Application {
    private static final int CELL_SIZE = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;
    private Cell[][] field = new Cell[20][15];
	private boolean canChangeDirection = true;

    private Game game;
    public void initialGame() {
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }

        Point point = new Point(10, 7);
        game = new Game(field, point);

    }
    public static void main(String[] args) {

        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        Assets.load();
        initialGame();
        Canvas canvas = new Canvas(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);


        scene.setOnKeyPressed(event -> {

            Snake snake = game.getSnake();
            switch (event.getCode()) {
	            case UP -> {
		            if (snake.getDirection() != Direction.DOWN && canChangeDirection) {
			            snake.setDirection(Direction.UP);
						canChangeDirection = false;
		            }
	            }

	            case DOWN -> {
					if (snake.getDirection() != Direction.UP && canChangeDirection) {
						snake.setDirection(Direction.DOWN);
						canChangeDirection = false;
					}

	            }
                case LEFT -> {
	                if(snake.getDirection() != Direction.RIGHT && canChangeDirection) {
		                snake.setDirection(Direction.LEFT);
						canChangeDirection = false;
	                }
                }
                case RIGHT -> {
	                if(snake.getDirection() != Direction.LEFT && canChangeDirection) {
		                snake.setDirection(Direction.RIGHT);
						canChangeDirection = false;
	                }
                }
            }
        });

        primaryStage.setTitle("Змейка");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {

                if (now - lastUpdate >= 100_000_000) {


                    game.step();


                    draw(gc);

                    lastUpdate = now;
					canChangeDirection = true;
                }
            }
        };
        timer.start();
    }
    private void draw(GraphicsContext gc) {
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if ((x + y) % 2 == 0) {
                    gc.drawImage(Assets.bg1, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    gc.drawImage(Assets.bg2, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                Cell currentCell = game.getField().field[x][y];

                if(currentCell == Cell.WALL) {
                    gc.drawImage(Assets.wall, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                if(currentCell == Cell.FOOD) {
                    gc.drawImage(Assets.apple, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
        Snake snake = game.getSnake();
        Direction temp = snake.getDirection();
        int size = snake.getBody().size();

        for(int i = 0; i < size; i++) {
            Point current = snake.getBody().get(i);

            if (i == 0) {
                drawRotatedImage(gc, Assets.snakeHead, current, temp);
            }

            else if (i == size - 1) {
                Point prev = snake.getBody().get(i - 1);
                Direction tailDir = getDirectionBetween(current, prev);
                drawRotatedImage(gc, Assets.snakeTail, current, tailDir);
            }

            else {
                Point prev = snake.getBody().get(i - 1);
                Point next = snake.getBody().get(i + 1);


                boolean isStraightX = (prev.x() == current.x() && current.x() == next.x());
                boolean isStraightY = (prev.y() == current.y() && current.y() == next.y());

                if (isStraightX || isStraightY) {

                    Direction bodyDir = getDirectionBetween(current, prev);
                    drawRotatedImage(gc, Assets.snakeBody, current, bodyDir);
                } else {

                    Direction dirToPrev = getDirectionBetween(current, prev);
                    Direction dirToNext = getDirectionBetween(current, next);

                    Direction cornerDir = getCornerDirection(dirToPrev, dirToNext);
                    drawRotatedImage(gc, Assets.snakeChange, current, cornerDir);
                }
            }
        }
    }
    private void drawRotatedImage(GraphicsContext gc, Image image, Point point, Direction direction) {
        int degree = 0;

        switch(direction) {
            case DOWN  -> degree = 90;
            case LEFT  -> degree = 180;
            case UP    -> degree = -90;
        }

        gc.save();


        gc.translate(point.x() * CELL_SIZE + CELL_SIZE / 2.0, point.y() * CELL_SIZE + CELL_SIZE / 2.0);

        gc.rotate(degree);


        gc.drawImage(image, -CELL_SIZE / 2.0, -CELL_SIZE / 2.0, CELL_SIZE, CELL_SIZE);

        gc.restore();
    }

    private Direction getCornerDirection(Direction d1, Direction d2) {

        if ((d1 == Direction.UP && d2 == Direction.RIGHT) || (d2 == Direction.UP && d1 == Direction.RIGHT)) {
            return Direction.RIGHT;
        }
        if ((d1 == Direction.RIGHT && d2 == Direction.DOWN) || (d2 == Direction.RIGHT && d1 == Direction.DOWN)) {
            return Direction.DOWN;
        }
        if ((d1 == Direction.DOWN && d2 == Direction.LEFT) || (d2 == Direction.DOWN && d1 == Direction.LEFT)) {
            return Direction.LEFT;
        }
        if ((d1 == Direction.LEFT && d2 == Direction.UP) || (d2 == Direction.LEFT && d1 == Direction.UP)) {
            return Direction.UP;
        }

        return Direction.RIGHT;
    }

    private Direction getDirectionBetween(Point from, Point to) {
        if (to.x() > from.x()) return Direction.RIGHT;
        if (to.x() < from.x()) return Direction.LEFT;

        if (to.y() > from.y()) return Direction.DOWN;
        if (to.y() < from.y()) return Direction.UP;

        return Direction.RIGHT;
    }
}
