package ru.nsu.ermakov;


import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputHandlerTest {
    @Test
    void testAskYesNoYes() {
        Scanner scanner = new Scanner("y\n");
        assertTrue(InputHandler.askYesNo(scanner, "Играть? "));
    }

    @Test
    void testAskYesNoNo() {
        Scanner scanner = new Scanner("n\n");
        assertEquals(false, InputHandler.askYesNo(scanner, "Играть? "));
    }

    @Test
    void testAskPlayerMoveHit() {
        Scanner scanner = new Scanner("1\n");
        assertEquals(Command.HIT, InputHandler.askPlayerMove(scanner));
    }

    @Test
    void testAskPlayerMoveStand() {
        Scanner scanner = new Scanner("0\n");
        assertEquals(Command.STAND, InputHandler.askPlayerMove(scanner));
    }

    @Test
    void testInvalidInputThrows() {
        Scanner scanner = new Scanner("abc\n");
        assertThrows(Exception.class, () -> InputHandler.askPlayerMove(scanner));
    }
}
