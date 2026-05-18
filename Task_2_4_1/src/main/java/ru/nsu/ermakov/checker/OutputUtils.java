package ru.nsu.ermakov.checker;

/**
 * Вспомогательные утилиты для обработки вывода и текста.
 */
public class OutputUtils {

    /**
     * Приватный конструктор для утилитарного класса.
     */
    private OutputUtils() {
    }

    /**
     * Возвращает первую непустую строку из текста.
     * @param text входной текст
     * @return первая строка или пустая строка
     */
    public static String firstLine(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String[] lines = text.split("\\R", 2);
        return lines[0].trim();
    }

    /**
     * Укорачивает текст до заданной длины, заменяя лишние пробелы.
     * @param text входной текст
     * @param maxLength максимальная длина
     * @return сокращённый текст
     */
    public static String shorten(String text, int maxLength) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String compact = text.replaceAll("\\s+", " ").trim();
        if (compact.length() <= maxLength) {
            return compact;
        }
        return compact.substring(0, maxLength) + "...";
    }
}
