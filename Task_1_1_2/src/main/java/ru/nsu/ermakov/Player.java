package ru.nsu.ermakov;

import java.util.ArrayList;
import java.util.List;

/**
 * Представляет игрока (или дилера) в игре Блэкджек.
 */
public class Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    /**
     * Создаёт игрока с заданным именем.
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
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Очищает руку.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Подсчитывает сумму очков на руке игрока.
     * Тузы считаются как 11, но могут уменьшаться до 1,
     * если сумма превышает 21.
     *
     * @return сумма очков
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

    /**
     * Возвращает список карт игрока.
     *
     * @return карты игрока
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Возвращает имя игрока.
     *
     * @return имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает строковое представление руки игрока.
     *
     * @param hideFirst если true, первая карта скрыта
     * @return строка с картами и очками
     */
    public String showHand(boolean hideFirst) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < hand.size(); i++) {
            if (i == 1 && hideFirst) {
                sb.append("<закрытая карта>");
            } else {
                sb.append(hand.get(i));
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
