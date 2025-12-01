import java.io.IOException;

/**
 * Главный класс для запуска программы, который использует другие классы для чтения файла и поиска подстроки.
 */
public class Main {

    /**
     * Главный метод программы. Открывает файл, читает его построчно и ищет подстроку.
     */
    public static void main(String[] args) {
        // Имя файла и подстрока для поиска
        String fileName = "largeFile.txt";
        String pattern = "искомуя";

        try {
            FileReader fileReader = new FileReader(fileName);
            String line;
            long currentPos = 0;

            while ((line = fileReader.readLine()) != null) {
                SubstringSearch.findSubstringInText(line, pattern, currentPos);
                currentPos += line.length() + 1;
            }

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
