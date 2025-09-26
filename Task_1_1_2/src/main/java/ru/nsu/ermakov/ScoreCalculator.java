package ru.nsu.ermakov;

import java.util.List;

/**
 * Утилита для подсчёта очков руки.
 */
public class ScoreCalculator {
    /**
     * Подсчитывает очки с учётом тузов (11 или 1).
     *
     * @param cards список карт
     * @return сумма очков
     */
    public static int calculate(List<Card> cards) {
        int total = 0;
        int aces = 0;

        for (Card card : cards) {
            total += card.getBaseValue();
            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}
