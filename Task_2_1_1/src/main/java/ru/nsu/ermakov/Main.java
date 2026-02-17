package ru.nsu.ermakov;

import ru.nsu.ermakov.checkers.ParallelChecker;
import ru.nsu.ermakov.checkers.SimpleChecker;
import ru.nsu.ermakov.checkers.ThreadChecker;
import java.util.Arrays;

/**
 * Main.
 */
public class Main {
    /**
     * Main.
     */
    public static void main(String[] args) throws InterruptedException {
        Thread workerThread = new Thread(() -> {
            try {
                System.out.println("[Worker]: Начало выполнения длительной операции (5000 мс)...");
                Thread.sleep(5000);
                System.out.println("[Worker]: Операция успешно завершена.");
            } catch (InterruptedException e) {
                System.err.println("[Worker]: Выполнение прервано во время сна.");
                Thread.currentThread().interrupt();
            }
        });

        Thread monitorThread = new Thread(() -> {
            System.out.println("[Monitor]: Ожидание завершения Worker-потока...");
            try {
                workerThread.join();
                System.out.println("[Monitor]: Worker-поток завершил работу. Мониторинг окончен.");
            } catch (InterruptedException e) {
                System.err.println("[Monitor]: Ошибка: поток был прерван во время ожидания (join).");
                System.err.println("[Monitor]: Стек вызовов: " + e.toString());
                Thread.currentThread().interrupt();
            }
        });

        workerThread.start();
        monitorThread.start();

        Thread.sleep(1000);

        System.out.println("\n[Main]: Инициация прерывания Monitor-потока...");
        monitorThread.interrupt();
    }
}
