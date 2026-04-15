package ru.nsu.ermakov.model;

/**
 * Интерфейс наблюдателя игры.
 */
public interface GameObserver {
    /**
     * Обновляет состояние наблюдателя.
     *
     * @param state состояние игры
     */
    void update(GameState state);
}
