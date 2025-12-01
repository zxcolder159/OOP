package ru.nsu.ermakov;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Класс для чтения файла построчно с использованием кодировки UTF-8.
 */
public class FileReader {
    private BufferedReader reader;

    /**
     * Конструктор для создания объекта чтения файла.
     */
    public FileReader(String fileName) throws IOException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
    }

    /**
     * Читает следующую строку из файла.
     */
    public String readLine() throws IOException {
        return reader.readLine();
    }

    /**
     * Закрывает BufferedReader.
     */
    public void close() throws IOException {
        reader.close();
    }
}
