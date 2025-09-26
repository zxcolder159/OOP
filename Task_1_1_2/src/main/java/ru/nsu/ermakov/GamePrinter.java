package ru.nsu.ermakov;

import java.util.List;

/**
 * Класс для вывода информации о ходе игры.
 */
public class GamePrinter {

    /**
     * Печатает стартовое сообщение.
     */
    public void printWelcome() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    /**
     * Печатает карты игрока.
     *
     * @param player игрок
     */
    public void printPlayerHand(Player player) {
        System.out.println("Ваши карты: " + player.getHand().toString(false));
    }

    /**
     * Печатает карты дилера.
     *
     * @param dealer    дилер
     * @param hideFirst скрыть ли первую карту
     */
    public void printDealerHand(Dealer dealer, boolean hideFirst) {
        System.out.println("Карты дилера: " + dealer.getHand().toString(hideFirst));
    }

    /**
     * Печатает сообщение, когда игрок получает карту.
     *
     * @param card карта
     */
    public void printPlayerDraw(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    /**
     * Печатает сообщение, когда дилер получает карту.
     *
     * @param card карта
     */
    public void printDealerDraw(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    /**
     * Печатает результат раунда.
     *
     * @param state состояние игры
     */
    public void printResult(GameState state) {
        switch (state) {
            case PLAYER_WIN:
                System.out.println("Вы выиграли раунд!");
                break;
            case DEALER_WIN:
                System.out.println("Дилер выиграл раунд!");
                break;
            case DRAW:
                System.out.println("Ничья!");
                break;
            default:
                // Обработка на случай новых состояний в будущем
                break;
        }
    }

    /**
     * Печатает текущий счёт.
     *
     * @param playerScore счёт игрока
     * @param dealerScore счёт дилера
     */
    public void printScore(int playerScore, int dealerScore) {
        System.out.printf("Счёт %d:%d (Вы : Дилер)%n", playerScore, dealerScore);
    }

    /**
     * Печатает список карт (для отладки).
     *
     * @param cards список карт
     */
    public void printCards(List<Card> cards) {
        System.out.println(cards);
    }
}
