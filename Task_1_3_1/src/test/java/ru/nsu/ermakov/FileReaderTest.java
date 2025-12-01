package ru.nsu.ermakov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * Тесты для класса FileReader.
 */
public class FileReaderTest {

    private static final String TEST_FILE = "testFile.txt";

    /**
     * Генерация тестового файла перед каждым тестом.
     */
    @BeforeEach
    public void setUp() throws IOException {
        // Генерация тестового файла перед каждым тестом
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("This is a test file.");
            writer.newLine();
            writer.write("File contains multiple lines.");
            writer.newLine();
            writer.write("Test file ends here.");
        }
    }

    /**
     * Тест на чтение файла и проверку строк.
     */
    @Test
    public void testReadFile() throws IOException {
        FileReader fileReader = new FileReader(TEST_FILE);
        String line = fileReader.readLine();
        assertEquals("This is a test file.", line, "Первая строка должна быть 'This is a test file.'");

        line = fileReader.readLine();
        assertEquals("File contains multiple lines.", line, "Вторая строка должна быть 'File contains multiple lines.'");

        line = fileReader.readLine();
        assertEquals("Test file ends here.", line, "Третья строка должна быть 'Test file ends here.'");

        line = fileReader.readLine();
        assertNull(line, "После чтения всех строк, должна быть возвращена null.");

        fileReader.close();
    }
}
