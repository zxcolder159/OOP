package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Негативные сценарии для парсера.
 */
public final class ParserErrorTest {

    /**
     * После корректного выражения не должно оставаться символов.
     * Ввод "1 2" содержит хвост, парсер должен бросить IllegalArgumentException.
     */
    @Test
    void rejectsTrailingInput() {
        assertThrows(IllegalArgumentException.class, () -> Parser.parse("1 2"));
    }
}
