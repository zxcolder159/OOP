package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для перечисления AppState.
 */
public class AppStateTest {
    /**
     * Проверяет значения перечисления.
     */
    @Test
    @DisplayName("AppState enum values are correct")
    public void testEnumValues() {
        AppState[] values = AppState.values();
        Assertions.assertEquals(4, values.length);
        Assertions.assertEquals(AppState.MENU, AppState.valueOf("MENU"));
        Assertions.assertEquals(AppState.PLAYING, AppState.valueOf("PLAYING"));
        Assertions.assertEquals(AppState.PAUSED, AppState.valueOf("PAUSED"));
        Assertions.assertEquals(AppState.GAME_OVER, AppState.valueOf("GAME_OVER"));
    }
}

