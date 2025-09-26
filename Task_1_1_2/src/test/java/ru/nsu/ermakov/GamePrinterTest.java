package ru.nsu.ermakov;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
class GamePrinterTest {
    @Test
    void testPrintsDoNotCrash() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer("Дилер");
        assertDoesNotThrow(() -> {
            GamePrinter.printWelcome();
            GamePrinter.printRoundStart(1);
            GamePrinter.printPlayerHand(player, false);
            GamePrinter.printDealerInitial(dealer);
            GamePrinter.printResult(GameResult.DRAW);
        });
    }
}
