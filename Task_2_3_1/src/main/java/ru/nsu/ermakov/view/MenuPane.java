package ru.nsu.ermakov.view;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ru.nsu.ermakov.game.Game;
import ru.nsu.ermakov.model.AppState;
import ru.nsu.ermakov.model.GameObserver;
import ru.nsu.ermakov.model.GameState;
import ru.nsu.ermakov.model.Level;
import ru.nsu.ermakov.model.LevelManager;

/**
 * Панель меню игры.
 */
public class MenuPane extends VBox implements GameObserver {
    private final Game game;
    private final Label descriptionLabel;
    private final VBox levelButtonsContainer;
    private int currentSelectedIndex = -1;

    /**
     * Конструктор панели меню.
     *
     * @param game объект игры
     */
    public MenuPane(Game game) {
        this.game = game;
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.85);");

        Label titleLabel = new Label("SNAKE");
        titleLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 64px; -fx-text-fill: #4CAF50;"
                + " -fx-font-weight: bold; "
                + "-fx-effect: dropshadow(gaussian, #2E7D32, 4, 0.5, 0, 2);");

        Label subtitleLabel = new Label("SELECT LEVEL");
        subtitleLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 28px; "
                + "-fx-text-fill: #FFFFFF;");

        levelButtonsContainer = new VBox(10);
        levelButtonsContainer.setAlignment(Pos.CENTER);
        buildLevelButtons();

        descriptionLabel = new Label();
        descriptionLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; "
                + "-fx-text-fill: #B0BEC5; -fx-alignment: center;");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(500);
        descriptionLabel.setAlignment(Pos.CENTER);
        updateDescription();

        Label hintLabel = new Label(
                "Click a level to start  |  M — menu  |  R — restart  |  ESC — pause");
        hintLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; "
                + "-fx-text-fill: #78909C;");

        getChildren().addAll(titleLabel, subtitleLabel, levelButtonsContainer,
                descriptionLabel, hintLabel);

        game.addObserver(this);
    }

    /**
     * Строит кнопки уровней.
     */
    private void buildLevelButtons() {
        levelButtonsContainer.getChildren().clear();
        List<Level> levels = LevelManager.getLevels();
        int selectedIndex = LevelManager.getSelectedLevelIndex();

        for (int i = 0; i < levels.size(); i++) {
            Level level = levels.get(i);
            Button btn = createLevelButton(level, i, i == selectedIndex);
            int index = i;
            btn.setOnAction(e -> {
                int prevSelected = LevelManager.getSelectedLevelIndex();
                if (prevSelected == index) {
                    game.startGame();
                } else {
                    LevelManager.setSelectedLevelIndex(index);
                    buildLevelButtons();
                    updateDescription();
                }
            });
            levelButtonsContainer.getChildren().add(btn);
        }
        currentSelectedIndex = selectedIndex;
    }

    /**
     * Создает кнопку уровня.
     *
     * @param level уровень
     * @param index индекс уровня
     * @param selected флаг выбранности
     * @return кнопка уровня
     */
    private Button createLevelButton(Level level, int index, boolean selected) {
        Button btn = new Button(level.getName());
        btn.setMaxWidth(400);
        btn.setMinWidth(400);
        btn.setMinHeight(44);

        String baseStyle = "-fx-font-family: 'Arial'; -fx-font-size: 18px;"
                + " -fx-cursor: hand; -fx-background-radius: 8; -fx-border-radius: 8;"
                + " -fx-border-width: 2; -fx-alignment: center;";

        if (selected) {
            baseStyle += " -fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF;"
                    + " -fx-border-color: #81C784; -fx-font-weight: bold;";
        } else {
            baseStyle += " -fx-background-color: #37474F; -fx-text-fill: #CFD8DC;"
                    + " -fx-border-color: #546E7A;";
        }

        String hoverStyle = "-fx-font-family: 'Arial'; -fx-font-size: 18px;"
                + " -fx-cursor: hand; -fx-background-radius: 8; -fx-border-radius: 8;"
                + " -fx-border-width: 2; -fx-alignment: center;"
                + " -fx-background-color: #66BB6A; -fx-text-fill: #FFFFFF;"
                + " -fx-border-color: #A5D6A7; -fx-font-weight: bold;";

        String normalStyle = "-fx-font-family: 'Arial'; -fx-font-size: 18px;"
                + " -fx-cursor: hand; -fx-background-radius: 8; -fx-border-radius: 8;"
                + " -fx-border-width: 2; -fx-alignment: center;"
                + " -fx-background-color: #37474F; -fx-text-fill: #CFD8DC;"
                + " -fx-border-color: #546E7A;";

        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> {
            if (index == LevelManager.getSelectedLevelIndex()) {
                btn.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px;"
                        + " -fx-cursor: hand; -fx-background-radius: 8; -fx-border-radius: 8;"
                        + " -fx-border-width: 2; -fx-alignment: center;"
                        + " -fx-background-color: #4CAF50; -fx-text-fill: #FFFFFF;"
                        + " -fx-border-color: #81C784; -fx-font-weight: bold;");
            } else {
                btn.setStyle(normalStyle);
            }
        });

        return btn;
    }

    /**
     * Обновляет описание выбранного уровня.
     */
    private void updateDescription() {
        Level selected = LevelManager.getSelectedLevel();
        descriptionLabel.setText(selected.getDescription());
    }

    /**
     * Обновляет состояние панели меню.
     *
     * @param state состояние игры
     */
    @Override
    public void update(GameState state) {
        AppState appState = state.getAppState();
        setVisible(appState == AppState.MENU);
        setManaged(appState == AppState.MENU);

        if (appState == AppState.MENU) {
            int newIndex = LevelManager.getSelectedLevelIndex();
            if (newIndex != currentSelectedIndex) {
                buildLevelButtons();
                updateDescription();
            }
        }
    }
}
