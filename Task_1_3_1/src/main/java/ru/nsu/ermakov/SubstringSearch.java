package ru.nsu.ermakov;

/**
 * Класс для поиска подстроки в строках текста.
 */
public class SubstringSearch {

    /**
     * Находит все вхождения подстроки в строке и выводит их позиции.
     *
     * @param text текст, в котором ищем подстроку.
     * @param pattern подстрока для поиска.
     * @param currentPos текущая позиция в файле для корректного вычисления абсолютной позиции.
     */
    public static void findSubstringInText(String text, String pattern, long currentPos) {
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            // Выводим индекс начала вхождения подстроки
            System.out.println("Найдено на позиции: " + (currentPos + index));
            index++;
        }
    }
}
