package ru.nsu.ermakov.checker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Выполняет внешние команды в указанной директории и собирает их вывод.
 */
public class CommandExecutor {

    /**
     * Запускает команду в рабочей директории и возвращает результат с выводом.
     * @param workingDirectory рабочая директория
     * @param timeoutSeconds таймаут выполнения в секундах
     * @param command список аргументов команды
     * @return результат выполнения с флагом успеха и выводом
     */
    public ExecResult run(Path workingDirectory, long timeoutSeconds, List<String> command) {
        if (workingDirectory == null || command == null || command.isEmpty()) {
            return ExecResult.failure("Invalid command arguments");
        }

        try {
            Path tempLog = Files.createTempFile("oop-checker-", ".log");
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(workingDirectory.toFile());
            processBuilder.environment().put("GIT_TERMINAL_PROMPT", "0");
            processBuilder.environment().put("GCM_INTERACTIVE", "never");
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(tempLog.toFile());

            Process process = processBuilder.start();
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                String output = Files.readString(tempLog);
                Files.deleteIfExists(tempLog);
                String cmd = String.join(" ", command);
                String message = output + "\nCommand timed out: " + cmd;
                return ExecResult.failure(message);
            }

            String output = Files.readString(tempLog);
            Files.deleteIfExists(tempLog);
            return new ExecResult(process.exitValue() == 0, output);
        } catch (IOException e) {
            return ExecResult.failure("I/O error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ExecResult.failure("Interrupted");
        }
    }
}
