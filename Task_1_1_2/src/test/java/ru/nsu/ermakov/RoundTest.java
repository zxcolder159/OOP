package ru.nsu.ermakov;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RoundTest {
    @Test
    void testPlayOneRound() {
        ByteArrayInputStream in = new ByteArrayInputStream("0\n".getBytes());
        Scanner scanner = new Scanner(in);
        GameStats stats = new GameStats();
        Round round = new Round(1, stats, scanner);

        assertDoesNotThrow(round::play);
    }
}
