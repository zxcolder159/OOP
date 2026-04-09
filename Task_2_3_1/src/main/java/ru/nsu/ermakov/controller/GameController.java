package ru.nsu.ermakov.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import ru.nsu.ermakov.game.Direction;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.view.GameView;

public class GameController {
    private final Game game;
    private final GameView view;
    private final Scene scene;
    private boolean canChangeDirection = true;

    public GameController(Game game, GameView view, Scene scene) {
        this.game = game;
        this.view = view;
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
            private long lastRender = 0;
            private long lastMove = 0;
            private static final long RENDER_INTERVAL = 16_666_666L;

            @Override
            public void handle(long now) {
                if (now - lastRender >= RENDER_INTERVAL) {
                    GameState state = game.getState();
                    view.render(state);
                    lastRender = now;
                }

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
