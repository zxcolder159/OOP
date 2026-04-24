package ru.nsu.ermakov.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Тесты для класса LevelManager.
 */
public class LevelManagerTest {
    /**
     * Проверяет, что список уровней не пуст и корректен.
     */
    @Test
    @DisplayName("Levels list is not empty and contains Level objects")
    public void testGetLevels() {
        List<Level> levels = LevelManager.getLevels();
        Assertions.assertFalse(levels.isEmpty());
        for (Level level : levels) {
            Assertions.assertNotNull(level);
        }
    }

    /**
     * Проверяет выбор уровня по индексу.
     */
    @Test
    @DisplayName("Selecting level by index works correctly")
    public void testSetSelectedLevelIndex() {
        int oldIndex = LevelManager.getSelectedLevelIndex();
        LevelManager.setSelectedLevelIndex(0);
        Assertions.assertEquals(0, LevelManager.getSelectedLevelIndex());
        LevelManager.setSelectedLevelIndex(oldIndex);
    }
}

