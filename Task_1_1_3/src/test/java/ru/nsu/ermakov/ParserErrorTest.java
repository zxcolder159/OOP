package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParserErrorTest {

    /**
     * После корректного выражения не должно оставаться символов.
     * Ввод "1 2" содержит хвост, парсер должен бросить IllegalArgumentException.
     */
    @Test
    void rejectsTrailingInput() {
        // Создаем экземпляр парсера
        Parser parser = new Parser("1 2");
        // Проверяем, что при вызове парсинга будет выброшено исключение IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> parser.parse("1 2"));
    }
}
