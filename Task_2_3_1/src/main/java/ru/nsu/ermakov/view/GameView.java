package ru.nsu.ermakov.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import ru.nsu.ermakov.game.Cell;
import ru.nsu.ermakov.game.Direction;
import ru.nsu.ermakov.game.Point;
import ru.nsu.ermakov.gui.Assets;
import ru.nsu.ermakov.model.GameState;

import java.util.List;

public class GameView {
    private final GraphicsContext gc;
    private final int cellSize;

    public GameView(GraphicsContext gc, int cellSize) {
        this.gc = gc;
        this.cellSize = cellSize;
        gc.setImageSmoothing(false);
    }

    public void render(GameState state) {
        int width = state.getWidth();
        int height = state.getHeight();
        Cell[][] field = state.getField();
        List<Point> snakeBody = state.getSnakeBody();
        Direction snakeDirection = state.getSnakeDirection();
        int score = state.getScore();
        boolean paused = state.isPaused();

        drawBackground(width, height);
        drawField(field, width, height);
        drawSnake(snakeBody, snakeDirection);
        drawScore(score, width);

        if (paused) {
            drawPauseOverlay(width, height);
        }
    }

    private void drawScore(int score, int width) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + score, 10, 25);
    }

    private void drawPauseOverlay(int width, int height) {
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, width * cellSize, height * cellSize);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", width * cellSize / 2.0, height * cellSize / 2.0);

        gc.setFont(new Font("Arial", 20));
        gc.fillText("Press ESC to resume", width * cellSize / 2.0, height * cellSize / 2.0 + 40);
    }

    private void drawBackground(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((x + y) % 2 == 0) {
                    gc.drawImage(Assets.bg1, x * cellSize, y * cellSize, cellSize, cellSize);
                } else {
                    gc.drawImage(Assets.bg2, x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void drawField(Cell[][] field, int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = field[x][y];
                if (cell == Cell.WALL) {
                    gc.drawImage(Assets.wall, x * cellSize, y * cellSize, cellSize, cellSize);
                } else if (cell == Cell.FOOD) {
                    gc.drawImage(Assets.apple, x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void drawSnake(List<Point> body, Direction direction) {
        int size = body.size();

        for (int i = 0; i < size; i++) {
            Point current = body.get(i);

            if (i == 0) {
                drawRotatedImage(Assets.snakeHead, current, direction);
            } else if (i == size - 1) {
                Point prev = body.get(i - 1);
                Direction tailDir = getDirectionBetween(current, prev);
                drawRotatedImage(Assets.snakeTail, current, tailDir);
            } else {
                Point prev = body.get(i - 1);
                Point next = body.get(i + 1);

                boolean isStraightX = (prev.x() == current.x() && current.x() == next.x());
                boolean isStraightY = (prev.y() == current.y() && current.y() == next.y());

                if (isStraightX || isStraightY) {
                    Direction bodyDir = getDirectionBetween(current, prev);
                    drawRotatedImage(Assets.snakeBody, current, bodyDir);
                } else {
                    Direction dirToPrev = getDirectionBetween(current, prev);
                    Direction dirToNext = getDirectionBetween(current, next);
                    Direction cornerDir = getCornerDirection(dirToPrev, dirToNext);
                    drawRotatedImage(Assets.snakeChange, current, cornerDir);
                }
            }
        }
    }

    private void drawRotatedImage(Image image, Point point, Direction direction) {
        int degree = switch (direction) {
            case DOWN -> 90;
            case LEFT -> 180;
            case UP -> -90;
            case RIGHT -> 0;
        };

        gc.save();
        gc.translate(point.x() * cellSize + cellSize / 2.0, point.y() * cellSize + cellSize / 2.0);
        gc.rotate(degree);
        gc.drawImage(image, -cellSize / 2.0, -cellSize / 2.0, cellSize, cellSize);
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
