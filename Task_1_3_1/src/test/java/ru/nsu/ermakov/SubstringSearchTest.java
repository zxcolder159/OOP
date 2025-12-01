package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Тесты для класса SubstringSearch.
 */
public class SubstringSearchTest {

    /**
     * Тест на нахождение подстроки в тексте.
     */
    @Test
    public void testFindSubstringInText() {
        String text = "This is a simple test string for testing.";
        String pattern = "test";

        // Перехватываем вывод в консоль
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Вызов метода поиска подстроки
        SubstringSearch.findSubstringInText(text, pattern, 0);

        // Проверяем, что вывод был корректным
        assertTrue(outContent.toString().contains("Найдено на позиции: 17"),
            "Подстрока должна быть найдена на позиции 17.");

        // Восстанавливаем стандартный вывод
        System.setOut(System.out);
    }

    /**
     * Тест, когда подстрока не найдена.
     */
    @Test
    public void testNoSubstringFound() {
        String text = "This is a simple string.";
        String pattern = "nonexistent";

        // Перехватываем вывод в консоль
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Вызов метода поиска подстроки
        SubstringSearch.findSubstringInText(text, pattern, 0);

        // Проверяем, что ничего не выводится (если подстрока не найдена)
        assertEquals("", outContent.toString(), "Не должно быть найдено вхождений подстроки.");

        // Восстанавливаем стандартный вывод
        System.setOut(System.out);
    }
}
