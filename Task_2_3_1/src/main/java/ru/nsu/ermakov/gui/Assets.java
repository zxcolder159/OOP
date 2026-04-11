package ru.nsu.ermakov.gui;

import javafx.scene.image.Image;

import java.util.Objects;


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

    public static void load() {
        try {
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
