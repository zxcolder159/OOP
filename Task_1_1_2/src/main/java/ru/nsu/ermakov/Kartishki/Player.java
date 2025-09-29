package ru.nsu.ermakov.Kartishki;

import java.util.List;

/**
 * Класс, представляющий игрока.
 */
public class Player {
    private final String name;
    private final Hand hand = new Hand();

    /**
     * Создаёт игрока.
     *
     * @param name имя игрока
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Добавляет карту в руку.
     *
     * @param card карта
     */
    public void takeCard(InputHandler.Card card) {
        hand.addCard(card);
    }

    /**
     * Очищает руку игрока.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Возвращает количество очков игрока.
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Проверяет, перебрал ли игрок.
     */
    public boolean isBusted() {
        return getScore() > 21;
    }

    /**
     * Возвращает карты игрока.
     */
    public List<InputHandler.Card> getCards() {
        return hand.getCards();
    }

    /**
     * Имя игрока.
     */
    public String getName() {
        return name;
    }

    /**
     * Рука игрока в виде строки.
     *
     * @param hideFirst скрыть первую карту (для дилера)
     */
    public String showHand(boolean hideFirst) {
        return hand.toString(hideFirst);
    }
}
