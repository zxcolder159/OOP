package ru.nsu.ermakov;

/**
 * Утилитный класс для вывода сообщений в консоль во время игры.
 * Все методы статические, чтобы удобно было вызывать из любого места.
 */
public final class GamePrinter {

    private GamePrinter() {
        // утилитный класс
    }

    /**
     * Печатает приветствие.
     */
    public static void printWelcome() {
        System.out.println("Добро пожаловать в Блэкджек!");
    }

    /**
     * Печатает итоговое сообщение об окончании игры и общий счёт.
     *
     * @param stats статистика игры
     */
    public static void printGoodbye(GameStats stats) {
        System.out.printf("Игра окончена. Счёт %d:%d (Вы : Дилер)%n",
                stats.getPlayerWins(), stats.getDealerWins());
    }

    /**
     * Печатает заголовок раунда.
     *
     * @param round номер раунда
     */
    public static void printRoundStart(int round) {
        System.out.println("\nРаунд " + round);
    }

    /**
     * Печатает карты игрока.
     *
     * @param player    игрок
     * @param hideFirst скрыть ли первую карту (обычно false для игрока)
     */
    public static void printPlayerHand(Player player, boolean hideFirst) {
        System.out.println("Ваши карты: " + player.showHand(hideFirst));
    }

    /**
     * Печатает стартовое состояние дилера после раздачи
     * (вторая карта закрыта).
     *
     * @param dealer дилер
     */
    public static void printDealerInitial(Dealer dealer) {
        System.out.println("Дилер раздал карты.");
        System.out.println("Карты дилера: " + dealer.showHand(true));
    }

    /**
     * Печатает вскрытие закрытой карты дилера и его текущую руку.
     *
     * @param dealer дилер
     */
    public static void printDealerReveal(Dealer dealer) {
        System.out.println("\nХод дилера.");
        System.out.println("Карты дилера: " + dealer.showHand(false));
    }

    /**
     * Печатает карту, которую вытянул дилер, и его текущую руку.
     *
     * @param card   вытянутая карта
     * @param dealer дилер
     */
    public static void printDealerDraw(Card card, Dealer dealer) {
        System.out.println("Дилер открывает карту: " + card);
        System.out.println("Карты дилера: " + dealer.showHand(false));
    }

    /**
     * Печатает сообщение о переборе у игрока.
     */
    public static void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли раунд.");
    }

    /**
     * Печатает результат раунда.
     *
     * @param result результат (победа игрока, дилера или ничья)
     */
    public static void printResult(GameResult result) {
        switch (result) {
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
                // на случай будущих значений enum
                break;
        }
    }
}
