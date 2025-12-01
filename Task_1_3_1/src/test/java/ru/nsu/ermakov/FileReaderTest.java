import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса FileReader.
 */
public class FileReaderTest {

    private static final String TEST_FILE = "testFile.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Генерация тестового файла перед каждым тестом
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("Hello, World!\n");
            writer.write("This is a test file.\n");
            writer.write("Test file ends here.");
        }
    }

    @AfterEach
    public void tearDown() {
        // Удаление тестового файла после каждого теста
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testReadLine() throws IOException {
        FileReader fileReader = new FileReader(TEST_FILE);
        String line = fileReader.readLine();
        assertEquals("Hello, World!", line, "Первая строка должна быть 'Hello, World!'");

        line = fileReader.readLine();
        assertEquals("This is a test file.", line, "Вторая строка должна быть 'This is a test file.'");

        line = fileReader.readLine();
        assertEquals("Test file ends here.", line, "Третья строка должна быть 'Test file ends here.'");

        line = fileReader.readLine();
        assertNull(line, "После чтения всех строк, должна быть возвращена null.");

        fileReader.close();
    }
}
