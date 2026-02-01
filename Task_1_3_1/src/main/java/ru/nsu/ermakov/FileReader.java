package ru.nsu.ermakov;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Класс для чтения файла построчно с использованием внутреннего буфера байтов.
 */
public class FileReader {
    private static final int BUFFER_SIZE = 8192;

    private final FileInputStream inputStream;
    private final byte[] buffer;
    private int bufferPos;
    private int bufferLimit;

    /**
     * Конструктор для создания объекта чтения файла.
     *
     * @param fileName имя файла для чтения.
     */
    public FileReader(String fileName) throws IOException {
        this.inputStream = new FileInputStream(fileName);
        this.buffer = new byte[BUFFER_SIZE];
        this.bufferPos = 0;
        this.bufferLimit = 0;
    }

    /**
     * Подчитывает данные в буфер.
     *
     * @return количество прочитанных байтов или -1 при достижении конца файла.
     */
    private int fillBuffer() throws IOException {
        bufferLimit = inputStream.read(buffer);
        bufferPos = 0;
        return bufferLimit;
    }

    /**
     * Читает следующую строку из файла.
     * Строка собирается из байтов до символа '\n', затем декодируется как UTF-8.
     *
     * @return следующая строка или null, если файл закончился.
     */
    public String readLine() throws IOException {
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();

        while (true) {
            if (bufferPos >= bufferLimit) {
                if (fillBuffer() == -1) {
                    // конец файла
                    if (lineBuffer.size() == 0) {
                        return null;
                    } else {
                        break; 
                    }
                }
            }

            byte b = buffer[bufferPos++];

            if (b == '\n') {
                break;
            }

            if (b != '\r') {
                lineBuffer.write(b);
            }
        }

        return lineBuffer.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * Закрывает входной поток.
     */
    public void close() throws IOException {
        inputStream.close();
    }
}
