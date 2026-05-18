package ru.nsu.ermakov.checker;

/**
 * Результат выполнения внешней команды.
 * @param success успех выполнения
 * @param output вывод команды
 */
public record ExecResult(boolean success, String output) {
    /**
     * Создаёт результат неуспеха с сообщением.
     * @param message сообщение
     * @return объект результата
     */
    public static ExecResult failure(String message) {
        return new ExecResult(false, message == null ? "" : message);
    }
}
