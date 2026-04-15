package ru.nsu.ermakov;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.nsu.ermakov.controller.GameController;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.gui.Assets;
import ru.nsu.ermakov.model.Cell;
import ru.nsu.ermakov.model.Level;
import ru.nsu.ermakov.model.LevelManager;
import ru.nsu.ermakov.model.Point;
import ru.nsu.ermakov.view.GameView;
import ru.nsu.ermakov.view.MenuPane;

public class SnakeApplication extends Application {
    private static final int CELL_SIZE = 40;
    private static final int WIDTH = 20;
    private static final int HEIGHT = 15;

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }

	/**
	 * Запускает JavaFX приложение.
	 *
	 * @param primaryStage главное окно приложения
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Assets.load();
		} catch (Exception e) {
			showErrorAlert("Ошибка загрузки ресурсов", 
					"Не удалось загрузить графические ресурсы игры.\n" +
					"Убедитесь, что все файлы изображений находятся в папке resources.",
					e);
			System.exit(1);
			return;
		}

		try {
			Game game = createGameFromLevel();

			Canvas canvas = new Canvas(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);
			GraphicsContext gc = canvas.getGraphicsContext2D();

			MenuPane menuPane = new MenuPane(game);
			menuPane.setPrefSize(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE);

			StackPane root = new StackPane(canvas, menuPane);
			Scene scene = new Scene(root);

			GameView view = new GameView(gc, CELL_SIZE);

			game.addObserver(view);

			GameController controller = new GameController(game, scene);

			primaryStage.setTitle("Змейка");
			primaryStage.setScene(scene);
			primaryStage.show();

			view.update(game.getState());

			controller.startGameLoop();
		} catch (Exception e) {
			showErrorAlert("Ошибка инициализации игры",
					"Произошла ошибка при запуске игры.",
					e);
			System.exit(1);
		}
	}

    /**
     * Создает игру с пустым полем.
     *
     * @return объект Game
     */
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

	/**
	 * Создает игру на основе выбранного уровня.
	 *
	 * @return объект Game
	 */
	private Game createGameFromLevel() {
		Level level = LevelManager.getSelectedLevel();
		Game game = new Game(level);

		String levelName = level.getName();
		if (levelName.contains("Easy")) {
			game.setMoveIntervalNanos(150_000_000L);
		} else if (levelName.contains("Hard")) {
			game.setMoveIntervalNanos(70_000_000L);
		}
		
		return game;
	}

	/**
	 * Показывает диалоговое окно с ошибкой.
	 *
	 * @param title заголовок ошибки
	 *
	 * @param message сообщение об ошибке
	 *
	 * @param e исключение (может быть null)
	 */
	private void showErrorAlert(String title, String message, Exception e) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		if (e != null) {
			alert.getDialogPane().setExpandableContent(
					new javafx.scene.control.TextArea("Детали ошибки:\n" + e.toString()));
		}
		alert.showAndWait();
	}
}
