package ru.nsu.ermakov.gui;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * Класс для загрузки графических ресурсов игры.
 */
public class Assets {
    public static Image snakeHead;
    public static Image snakeBody;
    public static Image snakeChange;
    public static Image snakeTail;
    public static Image bg1;
    public static Image bg2;
    public static Image apple;
    public static Image banana;
    public static Image wall;

    /**
     * Загружает все графические ресурсы игры.
     *
     * @throws Exception если не удалось загрузить ресурсы
     */
    public static void load() throws Exception {
        snakeHead = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/snake_head.png")));
        snakeBody = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/snake_body.png")));
        snakeChange = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/snake_change.png")));
        snakeTail = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/snake_tail.png")));
        bg1 = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/bg1.png")));
        bg2 = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/bg2.png")));
        apple = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/apple.png")));
        banana = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/banana.png")));
        wall = new Image(Objects.requireNonNull(
                Assets.class.getResourceAsStream("/wall.png")));
    }

}
