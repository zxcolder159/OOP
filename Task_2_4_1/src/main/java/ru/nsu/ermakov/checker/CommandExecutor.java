package ru.nsu.ermakov.checker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandExecutor {

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
                return ExecResult.failure(output + "\nCommand timed out: " + String.join(" ", command));
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
