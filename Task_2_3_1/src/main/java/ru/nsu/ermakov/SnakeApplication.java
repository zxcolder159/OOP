package ru.nsu.ermakov;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import ru.nsu.ermakov.game.Cell;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.game.Point;
import ru.nsu.ermakov.game.Snake;
import ru.nsu.ermakov.gui.Assets;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.StackPane;

public class SnakeApplication extends Application {
    private static final int CELL_SIZE = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;
    private Cell[][] field = new Cell[20][15];


    private Game game;
    public void initialGame() {
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }
        field[1][0] = Cell.WALL;
        field[2][0] = Cell.WALL;
        field[3][0] = Cell.WALL;
        field[4][0] = Cell.WALL;
        Point point = new Point(10, 7);
        game = new Game(field, point);

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

        primaryStage.setTitle("Змейка");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 100_000_000) {


                    draw(gc);
                    lastUpdate = now;
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
                if(field[x][y] == Cell.WALL) {
                    gc.drawImage(Assets.wall, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }

                if(field[x][y] == Cell.FOOD) {

                    gc.drawImage(Assets.apple, x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                }
            }
        }
        Snake snake = game.getSnake();
        for(int i = 0; i < snake.getBody().size(); i++) {
            Point x = snake.getBody().get(i);
            if (x == snake.getBody().getFirst()) {
                gc.drawImage(Assets.snakeHead, x.x() * CELL_SIZE, x.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
            if (x == snake.getBody().getLast()) {
                gc.drawImage(Assets.snakeTail, x.x() * CELL_SIZE, x.y() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

        }
    }
}
