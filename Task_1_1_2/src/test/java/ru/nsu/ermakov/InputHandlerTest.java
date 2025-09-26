package ru.nsu.ermakov;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    @Test
    void testAskPlayerMoveHit() {
        ByteArrayInputStream in = new ByteArrayInputStream("1\n".getBytes());
        Scanner scanner = new Scanner(in);
        assertEquals(Command.HIT, InputHandler.askPlayerMove(scanner));
    }

    @Test
    void testAskYesNo() {
        ByteArrayInputStream in = new ByteArrayInputStream("y\n".getBytes());
        Scanner scanner = new Scanner(in);
        assertTrue(InputHandler.askYesNo(scanner, "Играть?"));
    }
}
