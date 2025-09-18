package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий игрока (или дилера).
 */
public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    /**
     * Подсчет суммы очков (тузы считаются как 11 или 1).
     */
    public int getScore() {
        int total = 0;
        int aces = 0;
        for (Card card : hand) {
            total += card.getBaseValue();
            if ("Туз".equals(card.getRank())) {
                aces++;
            }
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    /**
     * Показать карты игрока.
     *
     * @param hideFirst скрыть ли первую карту (для дилера)
     */
    public String showHand(boolean hideFirst) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            if (i == 1 && hideFirst) {
                sb.append("<закрытая карта>");
            } else {
                sb.append(hand.get(i).toString());
            }
            if (i < hand.size() - 1) {
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
