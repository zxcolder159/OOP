package ru.nsu.ermakov.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.AppState;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.view.GameView;

public class GameController {
    private final Game game;
    private final Scene scene;
    private boolean canChangeDirection = true;

    /**
     * Конструктор контроллера игры.
     *
     * @param game объект игры
     *
     * @param scene сцена JavaFX
     */
    public GameController(Game game, Scene scene) {
        this.game = game;
        this.scene = scene;
        setupInputHandlers();
    }

    /**
     * Настраивает обработчики клавиатуры.
     */
    private void setupInputHandlers() {
        scene.setOnKeyPressed(event -> {
            AppState appState = game.getAppState();
            
            switch (event.getCode()) {
                case ENTER -> {
                    if (appState == AppState.GAME_OVER) {
                        game.setAppState(AppState.MENU);
                    }
                }
                case M -> {
                    if (appState == AppState.PLAYING || appState == AppState.PAUSED || appState == AppState.GAME_OVER) {
                        game.restart();
                        game.setAppState(AppState.MENU);
                    }
                }
                case R -> {
                    if (appState == AppState.PAUSED || appState == AppState.GAME_OVER) {
                        game.restart();
                    }
                }
                case ESCAPE -> {
                    if (appState == AppState.PLAYING || appState == AppState.PAUSED) {
                        game.togglePause();
                    }
                }
                case UP -> {
                    if (appState == AppState.PLAYING && canChangeDirection && game.changeSnakeDirection(Direction.UP)) {
                        canChangeDirection = false;
                    }
                }
                case DOWN -> {
                    if (appState == AppState.PLAYING && canChangeDirection && game.changeSnakeDirection(Direction.DOWN)) {
                        canChangeDirection = false;
                    }
                }
                case LEFT -> {
                    if (appState == AppState.PLAYING && canChangeDirection && game.changeSnakeDirection(Direction.LEFT)) {
                        canChangeDirection = false;
                    }
                }
                case RIGHT -> {
                    if (appState == AppState.PLAYING && canChangeDirection && game.changeSnakeDirection(Direction.RIGHT)) {
                        canChangeDirection = false;
                    }
                }
            }
        });
    }

	/**
	 * Запускает игровой цикл.
	 */
	public void startGameLoop() {
		AnimationTimer timer = new AnimationTimer() {
			private long lastMove = 0;

			@Override
			public void handle(long now) {
				long moveInterval = game.getMoveIntervalNanos();

				if (now - lastMove >= moveInterval) {
					game.step();
					lastMove = now;
					canChangeDirection = true;
				}
			}
		};
		timer.start();
	}
}
