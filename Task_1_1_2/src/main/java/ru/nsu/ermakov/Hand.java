package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий руку игрока.
 */
public class Hand {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавляет карту в руку.
     *
     * @param card карта
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Очищает руку.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Возвращает список карт.
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Подсчёт очков с учётом тузов.
     */
    public int getScore() {
        return ScoreCalculator.calculate(cards);
    }

    /**
     * Представление руки в виде строки.
     *
     * @param hideFirst скрыть первую карту (для дилера)
     */
    public String toString(boolean hideFirst) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < cards.size(); i++) {
            if (i == 1 && hideFirst) {
                sb.append("<закрытая карта>");
            } else {
                sb.append(cards.get(i));
            }
            if (i < cards.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        if (!hideFirst) {
            sb.append(" = ").append(getScore());
        }
        return sb.toString();
    }
}
