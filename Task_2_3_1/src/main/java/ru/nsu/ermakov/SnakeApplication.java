package ru.nsu.ermakov;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.nsu.ermakov.controller.GameController;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.gui.Assets;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Point;
import ru.nsu.ermakov.view.GameView;

public class SnakeApplication extends Application {
    private static final int CELL_SIZE = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    public static void main(String[] args) {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) {
		Assets.load();

		Game game = createGame();

		Canvas canvas = new Canvas(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		StackPane root = new StackPane(canvas);
		Scene scene = new Scene(root);

		GameView view = new GameView(gc, CELL_SIZE);

		// ДОБАВЛЕНО: Подписываем view на изменения game
		game.addObserver(view);

		// ИЗМЕНЕНО: Контроллеру больше не нужен view
		GameController controller = new GameController(game, scene);

		primaryStage.setTitle("Змейка");
		primaryStage.setScene(scene);
		primaryStage.show();

		// ДОБАВЛЕНО: Вызываем начальную отрисовку
		view.update(game.getState());

		controller.startGameLoop();
	}

    private Game createGame() {
        Cell[][] field = new Cell[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                field[i][j] = Cell.EMPTY;
            }
        }

        Point startPoint = new Point(10, 7);
        return new Game(field, startPoint);
    }
}
