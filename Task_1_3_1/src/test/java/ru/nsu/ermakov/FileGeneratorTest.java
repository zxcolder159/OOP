package ru.nsu.ermakov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;

/**
 * Тесты для класса FileGenerator.
 *
 * Этот класс содержит тесты для проверки корректности работы класса FileGenerator.
 * Тесты включают проверку успешного создания файла и наличие в нем строки с подстрокой "искомуя".
 */
public class FileGeneratorTest {

    private static final String TEST_FILE = "testGeneratedFile.txt";

    /**
     * Метод, который выполняется после каждого теста.
     * Удаляет тестовый файл, если он был создан.
     */
    @AfterEach
    public void tearDown() {
        // Удаление тестового файла после каждого теста
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Тестирует метод генерации файла.
     * Генерирует файл с 10 строками и проверяет его создание,
     * а также наличие хотя бы одной строки с подстрокой "искомуя".
     *
     * @throws IOException Если происходит ошибка при чтении файла.
     */
    @Test
    public void testGenerateFile() throws IOException {
        // Генерация файла с 10 строками
        FileGenerator.generateFile(TEST_FILE, 10);

        File file = new File(TEST_FILE);
        Assertions.assertTrue(file.exists(), "Файл должен быть создан.");

        // Проверим, что в файле есть хотя бы одна строка с подстрокой "искомуя"
        boolean foundSubstring = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("искомуя")) {
                    foundSubstring = true;
                    break;
                }
            }
        }

        Assertions.assertTrue(foundSubstring, "Файл должен содержать подстроку 'искомуя'.");
    }
}
