package ru.nsu.ermakov;

/**
 * Представляет игральную карту для игры Блэкджек.
 */
public class Card {
    private final String suit;
    private final String rank;

    /**
     * Создаёт карту.
     *
     * @param rank номинал карты (2–10, Валет, Дама, Король, Туз)
     * @param suit масть карты (Пики, Червы, Бубны, Трефы)
     */
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Возвращает номинал карты.
     *
     * @return номинал карты
     */
    public String getRank() {
        return rank;
    }

    /**
     * Возвращает масть карты.
     *
     * @return масть карты
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Возвращает базовое значение карты.
     * <ul>
     *   <li>числовые карты — их значение,</li>
     *   <li>картинки — 10,</li>
     *   <li>туз — 11 (корректировка до 1 выполняется в Player).</li>
     * </ul>
     *
     * @return значение карты
     */
    public int getBaseValue() {
        if (rank.matches("\\d+")) {
            return Integer.parseInt(rank);
        } else if ("Туз".equals(rank)) {
            return 11;
        } else {
            return 10;
        }
    }

    @Override
    public String toString() {
        return rank + " " + suit;
    }
}
