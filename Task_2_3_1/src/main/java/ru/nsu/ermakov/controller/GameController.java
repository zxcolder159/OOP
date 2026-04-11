package ru.nsu.ermakov.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.Direction;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.view.GameView;

public class GameController {
    private final Game game;
    private final Scene scene;
    private boolean canChangeDirection = true;

    public GameController(Game game, Scene scene) {
        this.game = game;
        this.scene = scene;
        setupInputHandlers();
    }

    private void setupInputHandlers() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE -> {
                    game.togglePause();
                }
                case UP -> {
                    if (canChangeDirection && game.changeSnakeDirection(Direction.UP)) {
                        canChangeDirection = false;
                    }
                }
                case DOWN -> {
                    if (canChangeDirection && game.changeSnakeDirection(Direction.DOWN)) {
                        canChangeDirection = false;
                    }
                }
                case LEFT -> {
                    if (canChangeDirection && game.changeSnakeDirection(Direction.LEFT)) {
                        canChangeDirection = false;
                    }
                }
                case RIGHT -> {
                    if (canChangeDirection && game.changeSnakeDirection(Direction.RIGHT)) {
                        canChangeDirection = false;
                    }
                }
            }
        });
    }

	public void startGameLoop() {
		AnimationTimer timer = new AnimationTimer() {
			private long lastMove = 0;

			@Override
			public void handle(long now) {
				long moveInterval = game.getMoveIntervalNanos();

				// Оставили только логику движения (step)
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
