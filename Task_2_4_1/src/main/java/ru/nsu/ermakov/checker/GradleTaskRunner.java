package ru.nsu.ermakov.checker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GradleTaskRunner {
    private final CommandExecutor commandExecutor;
    private final long commandTimeoutSeconds;

    public GradleTaskRunner(CommandExecutor commandExecutor) {
        this(commandExecutor, 300);
    }

    public GradleTaskRunner(CommandExecutor commandExecutor, long commandTimeoutSeconds) {
        this.commandExecutor = commandExecutor;
        this.commandTimeoutSeconds = commandTimeoutSeconds > 0 ? commandTimeoutSeconds : 300;
    }

    public ExecResult runCompile(Path projectPath) {
        return runGradle(projectPath, List.of("clean", "compileJava"));
    }

    public ExecResult runJavadoc(Path projectPath) {
        return runGradle(projectPath, List.of("javadoc"));
    }

    public ExecResult runCheckstyle(Path projectPath) {
        ExecResult mainCheck = runGradle(projectPath, List.of("checkstyleMain"));
        if (mainCheck.success() || !isMissingTaskError(mainCheck.output())) {
            return mainCheck;
        }
        return runGradle(projectPath, List.of("checkstyle"));
    }

    public ExecResult runTests(Path projectPath) {
        return runGradle(projectPath, List.of("test"));
    }

    private ExecResult runGradle(Path projectPath, List<String> tasks) {
        if (projectPath == null || tasks == null || tasks.isEmpty()) {
            return ExecResult.failure("Invalid build command arguments");
        }

        List<String> command = new ArrayList<>();
        Path gradlew = projectPath.resolve("gradlew");
        Path gradlewBat = projectPath.resolve("gradlew.bat");

        if (Files.exists(gradlew)) {
            command.add("bash");
            command.add("./gradlew");
        } else if (Files.exists(gradlewBat)) {
            command.add("cmd");
            command.add("/c");
            command.add("gradlew.bat");
        } else if (Files.exists(projectPath.resolve("build.gradle")) || Files.exists(projectPath.resolve("build.gradle.kts"))) {
            command.add("gradle");
        } else {
            return ExecResult.failure("No Gradle build found in " + projectPath);
        }

        command.add("--no-daemon");
        command.add("--console=plain");
        command.addAll(tasks);

        return commandExecutor.run(projectPath, commandTimeoutSeconds, command);
    }

    private boolean isMissingTaskError(String output) {
        if (output == null) {
            return false;
        }
        String lower = output.toLowerCase(Locale.ROOT);
        return lower.contains("task") && lower.contains("not found");
    }
}
