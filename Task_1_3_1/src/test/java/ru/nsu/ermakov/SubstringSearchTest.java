package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса SubstringSearch.
 */
public class SubstringSearchTest {

    /**
     * Тест для метода поиска подстроки в тексте.
     * Этот метод проверяет, что подстрока "test" найдена в строке на правильной позиции.
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
     * Тест для метода поиска подстроки, когда подстрока не найдена.
     * Этот метод проверяет, что при отсутствии подстроки в тексте
     * не будет сделан вывод в консоль.
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
        assertEquals("", outContent.toString(), 
            "Не должно быть найдено вхождений подстроки.");

        // Восстанавливаем стандартный вывод
        System.setOut(System.out);
    }
}
