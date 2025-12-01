package ru.nsu.ermakov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Тесты для класса FileGenerator.
 */
public class FileGeneratorTest {

    private static final String TEST_FILE = "testGeneratedFile.txt";

    /**
     * Удаление тестового файла после каждого теста.
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
     * Тест генерации файла с 10 строками.
     */
    @Test
    public void testGenerateFile() throws IOException {
        // Генерация файла с 10 строками
        FileGenerator.generateFile(TEST_FILE, 10);

        File file = new File(TEST_FILE);
        assertTrue(file.exists(), "Файл должен быть создан.");

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

        assertTrue(foundSubstring, "Файл должен содержать подстроку 'искомуя'.");
    }
}
