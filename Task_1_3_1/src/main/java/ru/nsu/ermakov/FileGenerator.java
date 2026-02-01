package ru.nsu.ermakov;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Класс для генерации тестовых файлов с заданным содержимым.
 */
public class FileGenerator {

    /**
     * Генерирует тестовый файл с указанным количеством строк.
     * Вставляет подстроку через каждые 1000 строк.
     */
    public static void generateFile(String fileName, int size) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        StringBuilder sb = new StringBuilder();

        // Наполняем файл повторяющимися фрагментами текста
        for (int i = 0; i < size; i++) {
            sb.append("Random data line " + i + "\n");
            if (i % 1000 == 0) {
                sb.append("искомуя\n"); // Вставляем искомую подстроку
            }
        }

        writer.write(sb.toString());
        writer.close();
    }

    /**
     * Главный метод для генерации тестового файла.
     */
    public static void main(String[] args) throws IOException {
        String fileName = "largeFile.txt";
        generateFile(fileName, 100000); // 100 000 строк
    }
}
