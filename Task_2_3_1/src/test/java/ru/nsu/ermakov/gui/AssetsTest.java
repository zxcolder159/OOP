package ru.nsu.ermakov.gui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Assets class.
 * Assets manages static image resources for the game.
 */
class AssetsTest {

    /**
     * Tests that all image fields are declared.
     */
    @Test
    @DisplayName("Assets should declare all required image fields")
    void testImageFieldsExist() {
        try {
            Assets.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNotNull(Assets.snakeHead, "snakeHead field should exist");
        Assertions.assertNotNull(Assets.snakeBody, "snakeBody field should exist");
        Assertions.assertNotNull(Assets.snakeChange, "snakeChange field should exist");
        Assertions.assertNotNull(Assets.snakeTail, "snakeTail field should exist");
        Assertions.assertNotNull(Assets.bg1, "bg1 field should exist");
        Assertions.assertNotNull(Assets.bg2, "bg2 field should exist");
        Assertions.assertNotNull(Assets.apple, "apple field should exist");
        Assertions.assertNotNull(Assets.banana, "banana field should exist");
        Assertions.assertNotNull(Assets.wall, "wall field should exist");
    }

    /**
     * Tests that load method can be called without throwing exceptions.
     */
    @Test
    @DisplayName("Assets load method should be callable")
    void testLoadMethodCallable() {
        Assertions.assertDoesNotThrow(() -> Assets.load(),
                "Load method should not throw exception when called");
    }

    /**
     * Tests that load method initializes image fields.
     */
    @Test
    @DisplayName("Assets load method should initialize image fields")
    void testLoadInitializesFields() {
        Assets.snakeHead = null;
        Assets.snakeBody = null;
        Assets.snakeChange = null;
        Assets.snakeTail = null;
        Assets.bg1 = null;
        Assets.bg2 = null;
        Assets.apple = null;
        Assets.banana = null;
        Assets.wall = null;

        try {
            Assets.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Assertions.assertNotNull(Assets.snakeHead, "snakeHead should be initialized");
        Assertions.assertNotNull(Assets.snakeBody, "snakeBody should be initialized");
        Assertions.assertNotNull(Assets.snakeChange, "snakeChange should be initialized");
        Assertions.assertNotNull(Assets.snakeTail, "snakeTail should be initialized");
        Assertions.assertNotNull(Assets.bg1, "bg1 should be initialized");
        Assertions.assertNotNull(Assets.bg2, "bg2 should be initialized");
        Assertions.assertNotNull(Assets.apple, "apple should be initialized");
        Assertions.assertNotNull(Assets.banana, "banana should be initialized");
        Assertions.assertNotNull(Assets.wall, "wall should be initialized");
    }
}
