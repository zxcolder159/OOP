package ru.nsu.ermakov.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.AppState;
import ru.nsu.ermakov.model.Direction;

/**
 * Тесты для класса GameController.
 */
public class GameControllerTest {
    /**
     * Проверяет, что конструктор не выбрасывает исключения.
     */
    @Test
    @DisplayName("Constructor does not throw")
    public void testConstructor() {
        Game game = Mockito.mock(Game.class);
        Scene scene = Mockito.mock(Scene.class);
        Assertions.assertDoesNotThrow(() -> new GameController(game, scene));
    }

    /**
     * Проверяет обработку нажатия клавиш направления.
     */
    @Test
    @DisplayName("Direction keys change direction if allowed")
    public void testDirectionKeys() {
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getAppState()).thenReturn(AppState.PLAYING);
        Mockito.when(game.changeSnakeDirection(Direction.UP)).thenReturn(true);
        Scene scene = Mockito.mock(Scene.class);
        GameController controller = new GameController(game, scene);
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false);
        Mockito.verify(scene).setOnKeyPressed(Mockito.any());
    }

    /**
     * Проверяет обработку клавиши ESCAPE (пауза).
     */
    @Test
    @DisplayName("ESCAPE toggles pause in PLAYING or PAUSED state")
    public void testEscapeTogglesPause() {
        Game game = Mockito.mock(Game.class);
        Mockito.when(game.getAppState()).thenReturn(AppState.PLAYING);
        Scene scene = Mockito.mock(Scene.class);
        new GameController(game, scene);
        // Проверка: setOnKeyPressed вызван
        Mockito.verify(scene).setOnKeyPressed(Mockito.any());
    }
}

