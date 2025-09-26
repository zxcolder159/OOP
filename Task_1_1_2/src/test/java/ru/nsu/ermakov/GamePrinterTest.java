package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GamePrinterTest {
    @Test
    void testPrintWelcome() {
        assertDoesNotThrow(GamePrinter::printWelcome);
    }

    @Test
    void testPrintRoundStart() {
        assertDoesNotThrow(() -> GamePrinter.printRoundStart(1));
    }
}
