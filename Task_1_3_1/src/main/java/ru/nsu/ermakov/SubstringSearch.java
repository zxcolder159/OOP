package ru.nsu.ermakov;

/**
 * Класс для поиска подстроки в строках текста.
 */
public class SubstringSearch {

    /**
     * Находит все вхождения подстроки в строке и выводит их позиции.
     *
     * @param text       текст, в котором ищем подстроку.
     * @param pattern    подстрока для поиска.
     * @param currentPos текущая позиция в файле для корректного вычисления абсолютной позиции.
     */
    public static void findSubstringInText(String text, String pattern, long currentPos) {
        if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty()) {
            return;
        }

        char[] txt = text.toCharArray();
        char[] pat = pattern.toCharArray();

        int n = txt.length;
        int m = pat.length;

        if (m > n) {
            return;
        }

        int alphabetSize = Character.MAX_VALUE + 1; // 65536
        int[] shift = new int[alphabetSize];

        for (int i = 0; i < alphabetSize; i++) {
            shift[i] = m;
        }


        for (int j = 0; j < m - 1; j++) {
            shift[pat[j]] = m - 1 - j;
        }

        int i = 0;

        while (i <= n - m) {
            int j = m - 1;

            while (j >= 0 && pat[j] == txt[i + j]) {
                j--;
            }

            if (j < 0) {
                System.out.println("Найдено на позиции: " + (currentPos + i));

                i += 1;
            } else {
                char badChar = txt[i + j];
                int shiftValue = shift[badChar];

                if (shiftValue <= 0) {
                    shiftValue = 1;
                }

                i += shiftValue;
            }
        }
    }
}
